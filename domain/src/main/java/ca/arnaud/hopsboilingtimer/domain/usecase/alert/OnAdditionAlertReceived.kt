package ca.arnaud.hopsboilingtimer.domain.usecase.alert

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleActionHandler
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleEvent
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleStateMachine
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.ScheduleStateUseCase
import javax.inject.Inject

class OnAdditionAlertReceived @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val scheduleRepository: ScheduleRepository,
    scheduleStateRepository: ScheduleStateRepository,
    stateMachine: AdditionScheduleStateMachine,
    actionHandler: AdditionScheduleActionHandler,
) : ScheduleStateUseCase<OnAdditionAlertReceived.Params, Unit>(
    jobExecutorProvider = jobExecutorProvider,
    scheduleStateRepository = scheduleStateRepository,
    stateMachine = stateMachine,
    actionHandler = actionHandler,
) {

    data class Params(
        val alertId: String,
    )

    override suspend fun buildRequest(params: Params) {
        val alerts = scheduleRepository.getSchedule()?.alerts ?: emptyList()
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
                sendStateEvent(AdditionScheduleEvent.TimerEnd)
            }

            null -> {
                // No-op
            }
        }
    }

    private suspend fun setNextAlert(nextAlert: AdditionAlert?) {
        when (nextAlert) {
            null -> {
                sendStateEvent(AdditionScheduleEvent.TimerEnd)
            }

            is AdditionAlert.End,
            is AdditionAlert.Next,
            is AdditionAlert.Start -> {
                scheduleRepository.setNextAlert(nextAlert)
            }
        }
    }
}