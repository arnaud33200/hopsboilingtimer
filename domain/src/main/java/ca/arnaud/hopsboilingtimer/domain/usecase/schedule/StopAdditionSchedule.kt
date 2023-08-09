package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleActionHandler
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleEvent
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleStateMachine
import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import javax.inject.Inject

class StopAdditionSchedule @Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    scheduleStateRepository: ScheduleStateRepository,
    stateMachine: AdditionScheduleStateMachine,
    actionHandler: AdditionScheduleActionHandler,
) : ScheduleStateUseCase<Unit, Unit>(
    coroutineContextProvider = coroutineContextProvider,
    scheduleStateRepository = scheduleStateRepository,
    stateMachine = stateMachine,
    actionHandler = actionHandler,
) {

    override suspend fun buildRequest(params: Unit) {
        sendStateEvent(AdditionScheduleEvent.Cancel)
    }
}