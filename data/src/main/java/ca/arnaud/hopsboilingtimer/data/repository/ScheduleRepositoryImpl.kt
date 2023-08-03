package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.data.datasource.ScheduleLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.common.doOnSuccess
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.model.schedule.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.getSchedule
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.UpdateAdditionAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleLocalDataSource: ScheduleLocalDataSource,
    private val timeProvider: TimeProvider,
) : ScheduleRepository {

    private val scheduleStatusFlow = MutableStateFlow<ScheduleStatus>(ScheduleStatus.Iddle)
    private val schedule: AdditionSchedule? get() = scheduleStatusFlow.value.getSchedule()

    private val nextAdditionAlert = MutableStateFlow<AdditionAlert?>(null)

    override suspend fun getScheduleFlow(): Flow<ScheduleStatus> {
        if (scheduleStatusFlow.value == ScheduleStatus.Iddle) {
            getAdditionSchedule()
            refreshAdditionSchedule()
        }

        return scheduleStatusFlow
    }

    override suspend fun getNextAlertFLow(): Flow<AdditionAlert?> {
        if (nextAdditionAlert.value == null) {
            updateNextAddition()
        }
        return nextAdditionAlert
    }

    override suspend fun setAdditionScheduleStatus(status: ScheduleStatus) {
        if (status == scheduleStatusFlow.value) {
            return
        }

        when (status) {
            is ScheduleStatus.Started -> {
                val schedule = status.schedule
                scheduleLocalDataSource.setSchedule(schedule)
                nextAdditionAlert.value = schedule.alerts.firstOrNull()
            }

            ScheduleStatus.Iddle,
            ScheduleStatus.Canceled,
            ScheduleStatus.Stopped -> {
                schedule?.let {
                    scheduleLocalDataSource.deleteSchedule(it)
                }
                nextAdditionAlert.value = null
            }

        }
        scheduleStatusFlow.value = status
    }

    private suspend fun refreshAdditionSchedule() {
        val additionSchedule = schedule ?: return
        val nextAlert = additionSchedule.getNextAlert(timeProvider.getNowLocalDateTime())
        if (nextAlert == null) {
            setAdditionScheduleStatus(ScheduleStatus.Stopped)
            return
        }

        setNextAlert(nextAlert)
    }

    override suspend fun setNextAlert(nextAlert: AdditionAlert) {
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
        return schedule ?: scheduleLocalDataSource.getSchedule().also {
            updateCachedSchedule(it)
        }
    }

    override suspend fun updateAdditionAlert(
        newAlert: AdditionAlert
    ): Response<AdditionAlert, UpdateAdditionAlert.UpdateAdditionAlertException> {
        return scheduleLocalDataSource.updateAdditionAlert(newAlert).also { response ->
            response.doOnSuccess { updateCachedSchedule(it) }
        }
    }

    private fun updateCachedSchedule(updatedAlert: AdditionAlert) {
        val currentSchedule = schedule ?: return
        val newAlerts = currentSchedule.alerts.toMutableList().apply {
            replaceAll { alert ->
                when (alert.id) {
                    updatedAlert.id -> updatedAlert
                    else -> alert
                }
            }
        }
        updateCachedSchedule(currentSchedule.copy(alerts = newAlerts))
    }

    private fun updateCachedSchedule(schedule: AdditionSchedule?) {
        scheduleStatusFlow.value = when (schedule) {
            null -> ScheduleStatus.Stopped
            else -> ScheduleStatus.Started(schedule)
        }
    }
}