package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.mapper.ScheduleStatusMapper
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleOptions
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleEvent
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleState
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleEventHandler
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleStateMachine
import ca.arnaud.hopsboilingtimer.domain.statemachine.toScheduleState
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StartAdditionSchedule @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val scheduleRepository: ScheduleRepository,
    private val stateMachine: AdditionScheduleStateMachine,
    private val actionHandler: AdditionScheduleEventHandler,
    private val statusMapper: ScheduleStatusMapper,
) : SuspendableUseCase<ScheduleOptions, Unit>(jobExecutorProvider) {

    override suspend fun buildRequest(params: ScheduleOptions) {
        val state = scheduleRepository.getScheduleFlow().first().toScheduleState()

        stateMachine.transition(
            state, AdditionScheduleEvent.TimerStart(params)
        )?.let { transition ->
            actionHandler.handle(transition)
            try {
                val status = statusMapper.mapTo(transition.toState)
                scheduleRepository.setAdditionScheduleStatus(status)
            } catch (exception: ScheduleStatusMapper.ScheduleStatusMapperError) {
                // No-op
            }
        }
    }
}