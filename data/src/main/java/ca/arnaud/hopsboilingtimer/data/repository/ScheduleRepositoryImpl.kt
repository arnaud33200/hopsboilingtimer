package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.data.datasource.ScheduleLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.common.Response
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

    private var scheduleFlow: MutableStateFlow<AdditionSchedule?>? = null
    private val nextAdditionAlert = MutableStateFlow<AdditionAlert?>(null)

    override suspend fun getScheduleFLow(): Flow<AdditionSchedule?> {
        return scheduleFlow ?: MutableStateFlow(scheduleLocalDataSource.getSchedule()).also {
            scheduleFlow = it
        }
    }

    override suspend fun getNextAlertFLow(): Flow<AdditionAlert?> {

        return nextAdditionAlert
    }

    override suspend fun setNextAlert(nextAlert: AdditionAlert?) {
        if (nextAdditionAlert.value != nextAlert) {
            nextAdditionAlert.value = nextAlert
        }
    }

    override suspend fun getSchedule(): AdditionSchedule? {
        return scheduleFlow?.value
    }

    override suspend fun setSchedule(schedule: AdditionSchedule) {
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
            when (response) {
                is Response.Success -> updateCachedSchedule(response.data)
                is Response.Failure -> {} // No-op
            }
        }
    }

    private suspend fun updateCachedSchedule(updatedAlert: AdditionAlert) {
        val currentSchedule = getSchedule() ?: return
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
        this.scheduleFlow?.value = schedule
    }
}