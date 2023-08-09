package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleActionHandler
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleEvent
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleParams
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleStateMachine
import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import kotlinx.coroutines.flow.first

abstract class ScheduleStateUseCase<in T, out S> constructor(
    coroutineContextProvider: CoroutineContextProvider,
    private val scheduleStateRepository: ScheduleStateRepository,
    private val stateMachine: AdditionScheduleStateMachine,
    private val actionHandler: AdditionScheduleActionHandler,
) : SuspendableUseCase<T, S>(coroutineContextProvider) {

    suspend fun sendStateEvent(
        event: AdditionScheduleEvent,
        params: AdditionScheduleParams? = null,
    ) {
        val state = scheduleStateRepository.getScheduleStatusFlow().first()
        stateMachine.transition(state, event, params)?.let { transition ->
            actionHandler.handle(transition)
            scheduleStateRepository.setScheduleStatus(transition.toState)
        }
    }
}