package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsSuspendableUseCase
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SubscribeAdditionSchedule @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val scheduleRepository: ScheduleRepository,
) : NoParamsSuspendableUseCase<StateFlow<AdditionSchedule?>>(jobExecutorProvider) {

    override suspend fun buildRequest(): StateFlow<AdditionSchedule?> {
        return scheduleRepository.getScheduleFlow()
    }
}