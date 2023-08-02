package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsSuspendableUseCase
import javax.inject.Inject

class StopAdditionSchedule @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val scheduleRepository: ScheduleRepository
) : NoParamsSuspendableUseCase<Unit>(jobExecutorProvider) {

    override suspend fun buildRequest() {
        scheduleRepository.setAdditionScheduleStatus(ScheduleStatus.Canceled)
    }
}