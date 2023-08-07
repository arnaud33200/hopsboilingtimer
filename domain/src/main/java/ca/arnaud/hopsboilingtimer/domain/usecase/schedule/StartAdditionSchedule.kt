package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleOptions
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleActionHandler
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleEvent
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleParams
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleStateMachine
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import javax.inject.Inject

class StartAdditionSchedule @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    scheduleStateRepository: ScheduleStateRepository,
    stateMachine: AdditionScheduleStateMachine,
    actionHandler: AdditionScheduleActionHandler,
) : ScheduleStateUseCase<ScheduleOptions, Unit>(
    jobExecutorProvider = jobExecutorProvider,
    scheduleStateRepository = scheduleStateRepository,
    stateMachine = stateMachine,
    actionHandler = actionHandler,
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