package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsSuspendableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeAdditionSchedule @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val scheduleStateRepository: ScheduleStateRepository,
) : NoParamsSuspendableUseCase<Flow<ScheduleStatus>>(jobExecutorProvider) {

    override suspend fun buildRequest(): Flow<ScheduleStatus> {
        return scheduleStateRepository.getScheduleStatusFlow()
    }
}