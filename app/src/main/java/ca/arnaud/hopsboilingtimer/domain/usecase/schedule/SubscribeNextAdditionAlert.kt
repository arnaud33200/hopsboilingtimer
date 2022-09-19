package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsSuspendableUseCase
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SubscribeNextAdditionAlert @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val scheduleRepository: ScheduleRepository,
) : NoParamsSuspendableUseCase<StateFlow<AdditionAlert?>>(jobExecutorProvider) {

    override suspend fun buildRequest(): StateFlow<AdditionAlert?> {
        return scheduleRepository.getNextAlertFLow()
    }
}