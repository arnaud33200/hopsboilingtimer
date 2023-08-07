package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleState
import ca.arnaud.hopsboilingtimer.domain.model.schedule.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import javax.inject.Inject

class InitializeScheduleState @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val scheduleRepository: ScheduleRepository,
    private val scheduleStateRepository: ScheduleStateRepository,
    private val timeProvider: TimeProvider,
) : SuspendableUseCase<Unit, Unit>(jobExecutorProvider) {

    override suspend fun buildRequest(params: Unit) {
        val schedule = scheduleRepository.getSchedule()
        val nextAlert = schedule?.getNextAlert(timeProvider.getNowLocalDateTime())

        val initialState = when {
            schedule == null -> ScheduleState.Idle
            nextAlert == null -> ScheduleState.Idle
            else -> ScheduleState.Started
        }

        if (nextAlert != null) {
            scheduleRepository.setNextAlert(nextAlert)
        }

        scheduleStateRepository.setScheduleStatus(initialState)
    }
}