package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.data.datasource.ScheduleLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.UpdateAdditionAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Optional
import javax.inject.Inject
import kotlin.jvm.optionals.getOrNull

class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleLocalDataSource: ScheduleLocalDataSource,
) : ScheduleRepository {

    private val scheduleFlow = MutableStateFlow<Optional<AdditionSchedule>?>(null)
    private val nextAdditionAlert = MutableStateFlow<AdditionAlert?>(null)

    // region Schedule
    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun getScheduleFLow(): Flow<AdditionSchedule?> {
        if (scheduleFlow.value == null) {
            scheduleFlow.value = Optional.ofNullable(scheduleLocalDataSource.getSchedule())
        }
        return scheduleFlow.map { optional ->
            optional?.getOrNull()
        }
    }

    override suspend fun getSchedule(): AdditionSchedule? {
        return getScheduleFLow().first()
    }

    override suspend fun setSchedule(schedule: AdditionSchedule) {
        scheduleLocalDataSource.setSchedule(schedule)
        updateScheduleFlow(schedule)
    }

    override suspend fun deleteSchedule(schedule: AdditionSchedule) {
        scheduleLocalDataSource.deleteSchedule(schedule)
        updateScheduleFlow(null)
    }

    private fun updateScheduleFlow(schedule: AdditionSchedule?) {
        this.scheduleFlow.value = Optional.ofNullable(schedule)
    }

    // endregion

    // region Alert

    override suspend fun getNextAlertFLow(): Flow<AdditionAlert?> {
        return nextAdditionAlert
    }

    override suspend fun setNextAlert(nextAlert: AdditionAlert?) {
        if (nextAdditionAlert.value != nextAlert) {
            nextAdditionAlert.value = nextAlert
        }
    }

    override suspend fun updateAdditionAlert(
        newAlert: AdditionAlert
    ): Response<AdditionAlert, UpdateAdditionAlert.UpdateAdditionAlertException> {
        return scheduleLocalDataSource.updateAdditionAlert(newAlert).also { response ->
            when (response) {
                is Response.Success -> updateScheduleAlert(response.data)
                is Response.Failure -> {} // No-op
            }
        }
    }

    private suspend fun updateScheduleAlert(updatedAlert: AdditionAlert) {
        val currentSchedule = getSchedule() ?: return
        val newAlerts = currentSchedule.alerts.toMutableList().apply {
            replaceAll { alert ->
                when (alert.id) {
                    updatedAlert.id -> updatedAlert
                    else -> alert
                }
            }
        }
        updateScheduleFlow(currentSchedule.copy(alerts = newAlerts))
    }

    // endregion
}