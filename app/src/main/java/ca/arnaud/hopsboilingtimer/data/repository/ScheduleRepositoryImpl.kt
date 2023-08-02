package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.data.datasource.ScheduleLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.common.doOnSuccess
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.model.schedule.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.UpdateAdditionAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleLocalDataSource: ScheduleLocalDataSource,
    private val timeProvider: TimeProvider,
) : ScheduleRepository {

    private val scheduleStatusFlow = MutableStateFlow<AdditionSchedule?>(null)
    private val nextAdditionAlert = MutableStateFlow<AdditionAlert?>(null)

    private var schedule: AdditionSchedule? = null

    override suspend fun getScheduleFlow(): Flow<ScheduleStatus> {
        if (scheduleStatusFlow.value == null) {
            getAdditionSchedule()
            refreshAdditionSchedule()
            scheduleStatusFlow.value = schedule
        }

        return scheduleStatusFlow.map {  schedule ->
            when (schedule) {
                null -> ScheduleStatus.Stopped
                else -> ScheduleStatus.Started(schedule)
            }
        }
    }

    override suspend fun getNextAlertFLow(): StateFlow<AdditionAlert?> {
        if (nextAdditionAlert.value == null) {
            updateNextAddition()
        }
        return nextAdditionAlert
    }

    override suspend fun setAdditionSchedule(additionSchedule: AdditionSchedule?) {
        schedule = additionSchedule
        val nextAlert = schedule?.getNextAlert(timeProvider.getNowLocalDateTime())
        if (additionSchedule == null || nextAlert == null) {
            deleteSchedule()
            return
        }

        scheduleLocalDataSource.setSchedule(additionSchedule)
        if (additionSchedule != scheduleStatusFlow.value) {
            scheduleStatusFlow.value = additionSchedule
        }

        nextAdditionAlert.value = additionSchedule.alerts.firstOrNull()
    }

    override suspend fun refreshAdditionSchedule() {
        val additionSchedule = schedule ?: return
        val nextAlert = additionSchedule.getNextAlert(timeProvider.getNowLocalDateTime())
        if (nextAlert == null) {
            deleteSchedule()
            return
        }

        if (nextAdditionAlert.value != nextAlert) {
            nextAdditionAlert.value = nextAlert
        }
    }

    private fun updateNextAddition() {
        val currentSchedule = schedule ?: return

        val nextAlert = currentSchedule.getNextAlert(timeProvider.getNowLocalDateTime())

        if (nextAdditionAlert.value != nextAlert) {
            nextAdditionAlert.value = nextAlert
        }
    }

    override suspend fun getAdditionSchedule(): AdditionSchedule? {
        return schedule ?: scheduleLocalDataSource.getSchedule().also { schedule = it }
    }

    override suspend fun deleteSchedule() {
        schedule?.let { scheduleLocalDataSource.deleteSchedule(it) }
        schedule = null
        scheduleStatusFlow.value = null
        nextAdditionAlert.value = null
    }

    override suspend fun updateAdditionAlert(newAlert: AdditionAlert): Response<AdditionAlert, UpdateAdditionAlert.UpdateAdditionAlertException> {
        return scheduleLocalDataSource.updateAdditionAlert(newAlert).also { response ->
            response.doOnSuccess { updateCachedSchedule(it) }
        }
    }

    private fun updateCachedSchedule(updatedAlert: AdditionAlert) {
        val currentSchedule = schedule ?: return
        val newAlerts = currentSchedule.alerts.toMutableList().apply {
            replaceAll { alert ->
                when {
                    alert.id == updatedAlert.id -> updatedAlert
                    else -> alert
                }
            }
        }
        updateCachedSchedule(currentSchedule.copy(alerts = newAlerts))
    }

    private fun updateCachedSchedule(schedule: AdditionSchedule) {
        if (this.schedule == schedule) {
            return
        }
        this.schedule = schedule
        scheduleStatusFlow.value = schedule
    }
}