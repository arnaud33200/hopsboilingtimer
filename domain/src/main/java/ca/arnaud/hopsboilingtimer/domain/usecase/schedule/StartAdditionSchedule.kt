package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.mapper.ScheduleStatusMapper
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleOptions
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleEvent
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleEventHandler
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleParams
import ca.arnaud.hopsboilingtimer.domain.statemachine.AdditionScheduleStateMachine
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import javax.inject.Inject

class StartAdditionSchedule @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    scheduleStateRepository: ScheduleStateRepository,
    stateMachine: AdditionScheduleStateMachine,
    actionHandler: AdditionScheduleEventHandler,
    statusMapper: ScheduleStatusMapper,
) : ScheduleStateUseCase<ScheduleOptions, Unit>(
    jobExecutorProvider = jobExecutorProvider,
    scheduleStateRepository = scheduleStateRepository,
    stateMachine = stateMachine,
    actionHandler = actionHandler,
    statusMapper = statusMapper,
) {

    override suspend fun buildRequest(params: ScheduleOptions) {
        sendStateEvent(
            event = AdditionScheduleEvent.TimerStart,
            params = AdditionScheduleParams.Start(
                scheduleOptions = params
            )
        )
    }
}