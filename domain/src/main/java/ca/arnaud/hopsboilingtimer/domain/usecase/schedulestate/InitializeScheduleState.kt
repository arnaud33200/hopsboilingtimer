package ca.arnaud.hopsboilingtimer.domain.usecase.schedulestate

import ca.arnaud.hopsboilingtimer.domain.model.schedule.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleState
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject
import javax.inject.Named

class InitializeScheduleState @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    @Named("InitScheduleState")
    private val initCompletion: CompletableDeferred<Unit>,
    private val scheduleRepository: ScheduleRepository,
    private val scheduleStateRepository: ScheduleStateRepository,
    private val timeProvider: TimeProvider,
) : SuspendableUseCase<Unit, Unit>(jobExecutorProvider) {

    override suspend fun buildRequest(params: Unit) {
        val schedule = scheduleRepository.getSchedule()
        val nextAlert = schedule?.getNextAlert(timeProvider.getNowLocalDateTime())

        val initialState = when {
            schedule == null -> AdditionScheduleState.Idle
            nextAlert == null -> AdditionScheduleState.Idle
            else -> AdditionScheduleState.Started
        }

        if (nextAlert != null) {
            scheduleRepository.setNextAlert(nextAlert)
        }

        scheduleStateRepository.setScheduleStatus(initialState)
        initCompletion.complete(Unit)
    }
}