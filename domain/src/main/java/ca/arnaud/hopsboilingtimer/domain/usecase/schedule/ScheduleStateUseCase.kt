package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.mapper.ScheduleStatusMapper
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleEvent
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleEventHandler
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleState
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleStateMachine
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import kotlinx.coroutines.flow.first

abstract class ScheduleStateUseCase<in T, out S> constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val scheduleStateRepository: ScheduleStateRepository,
    private val stateMachine: AdditionScheduleStateMachine,
    private val actionHandler: AdditionScheduleEventHandler,
    private val statusMapper: ScheduleStatusMapper,
) : SuspendableUseCase<T, S>(jobExecutorProvider) {

    suspend fun sendStateEvent(event: AdditionScheduleEvent) {
        val state = scheduleStateRepository.getScheduleStatusFlow().first().toScheduleState()
        stateMachine.transition(state, event)?.let { transition ->
            actionHandler.handle(transition)
            try {
                val status = statusMapper.mapTo(transition.toState)
                scheduleStateRepository.setScheduleStatus(status)
            } catch (exception: ScheduleStatusMapper.ScheduleStatusMapperError) {
                // No-op
            }
        }
    }

    private fun ScheduleStatus.toScheduleState(): AdditionScheduleState {
        return when (this) {
            ScheduleStatus.Canceled -> AdditionScheduleState.Canceled
            ScheduleStatus.Iddle -> AdditionScheduleState.Iddle
            is ScheduleStatus.Started -> AdditionScheduleState.Going
            ScheduleStatus.Stopped -> AdditionScheduleState.Stopped
        }
    }
}