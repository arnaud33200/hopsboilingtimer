package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import javax.inject.Inject

class OnAdditionAlertReceived @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val scheduleRepository: ScheduleRepository,
) : SuspendableUseCase<OnAdditionAlertReceived.Params, Unit>(jobExecutorProvider) {

    data class Params(
        val alertId: String,
    )

    override suspend fun buildRequest(params: Params) {
        val alerts = scheduleRepository.getAdditionSchedule()?.alerts ?: emptyList()
        when (
            val receivedAlert = alerts.find { alert -> alert.id == params.alertId }
        ) {
            is AdditionAlert.Start,
            is AdditionAlert.Next -> {
                val nextAlertIndex = alerts.indexOf(receivedAlert) + 1
                val nextAlert = alerts.getOrNull(nextAlertIndex)
                setNextAlert(nextAlert)
            }

            is AdditionAlert.End -> {
                scheduleRepository.setAdditionScheduleStatus(ScheduleStatus.Stopped)
            }

            null -> {
                // No-op
            }
        }
    }

    private suspend fun setNextAlert(nextAlert: AdditionAlert?) {
        when (nextAlert) {
            null -> {
                scheduleRepository.setAdditionScheduleStatus(ScheduleStatus.Stopped)
            }

            is AdditionAlert.End,
            is AdditionAlert.Next,
            is AdditionAlert.Start -> {
                scheduleRepository.setNextAlert(nextAlert)
            }
        }
    }
}