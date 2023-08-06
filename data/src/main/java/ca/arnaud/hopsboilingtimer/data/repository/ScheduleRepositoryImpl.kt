package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.data.datasource.ScheduleLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.common.doOnSuccess
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.UpdateAdditionAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleLocalDataSource: ScheduleLocalDataSource,
) : ScheduleRepository {

    private var schedule: AdditionSchedule? = null

    private val nextAdditionAlert = MutableStateFlow<AdditionAlert?>(null)

    override suspend fun getNextAlertFLow(): Flow<AdditionAlert?> {

        return nextAdditionAlert
    }

    override suspend fun setNextAlert(nextAlert: AdditionAlert?) {
        if (nextAdditionAlert.value != nextAlert) {
            nextAdditionAlert.value = nextAlert
        }
    }

    override suspend fun getAdditionSchedule(): AdditionSchedule? {
        return schedule ?: scheduleLocalDataSource.getSchedule().also {
            updateCachedSchedule(it)
        }
    }

    override suspend fun setAdditionSchedule(schedule: AdditionSchedule) {
        scheduleLocalDataSource.setSchedule(schedule)
        updateCachedSchedule(schedule)
    }

    override suspend fun deleteSchedule(schedule: AdditionSchedule) {
        scheduleLocalDataSource.deleteSchedule(schedule)
        updateCachedSchedule(null)
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
        this.schedule = schedule
    }
}