package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleEvent
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleEventHandler
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleParams
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleStateMachine
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import kotlinx.coroutines.flow.first

abstract class ScheduleStateUseCase<in T, out S> constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val scheduleStateRepository: ScheduleStateRepository,
    private val stateMachine: AdditionScheduleStateMachine,
    private val actionHandler: AdditionScheduleEventHandler,
) : SuspendableUseCase<T, S>(jobExecutorProvider) {

    suspend fun sendStateEvent(
        event: AdditionScheduleEvent,
        params: AdditionScheduleParams? = null,
    ) {
        val state = scheduleStateRepository.getScheduleStatusFlow().first()
        stateMachine.transition(state, event, params)?.let { transition ->
            actionHandler.handle(transition)
            scheduleStateRepository.setScheduleStatus(state)
        }
    }
}