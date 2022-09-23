package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.data.datasource.ScheduleLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.model.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleLocalDataSource: ScheduleLocalDataSource,
    private val timeProvider: TimeProvider
) : ScheduleRepository {

    private val scheduleStatusFlow = MutableStateFlow<AdditionSchedule?>(null)
    private val nextAdditionAlert = MutableStateFlow<AdditionAlert?>(null)

    private var schedule: AdditionSchedule? = null

    override suspend fun getScheduleFlow(): StateFlow<AdditionSchedule?> {
        if (scheduleStatusFlow.value == null) {
            getAdditionSchedule()
            refreshAdditionSchedule()
            scheduleStatusFlow.value = schedule
        }

        return scheduleStatusFlow
    }

    override suspend fun getNextAlertFLow(): StateFlow<AdditionAlert?> {
        if (nextAdditionAlert.value == null) {
            updateNextAddition()
        }
        return nextAdditionAlert
    }

    override suspend fun setAdditionSchedule(additionSchedule: AdditionSchedule?) {
        schedule = additionSchedule
        val nextAlert = schedule?.getNextAlert(timeProvider.getNowTimeMillis())
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
        val nextAlert = additionSchedule.getNextAlert(timeProvider.getNowTimeMillis())
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

        val nextAlert = currentSchedule.getNextAlert(timeProvider.getNowTimeMillis())

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
}