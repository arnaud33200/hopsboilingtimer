package ca.arnaud.hopsboilingtimer.domain.usecase.alert

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsSuspendableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeNextAdditionAlert @Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    private val scheduleRepository: ScheduleRepository,
) : NoParamsSuspendableUseCase<Flow<AdditionAlert?>>(coroutineContextProvider) {

    override suspend fun buildRequest(): Flow<AdditionAlert?> {
        return scheduleRepository.getNextAlertFLow()
    }
}