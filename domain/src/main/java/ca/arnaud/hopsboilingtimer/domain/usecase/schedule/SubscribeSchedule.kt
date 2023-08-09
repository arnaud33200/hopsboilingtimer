package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsSuspendableUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeSchedule @Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    private val scheduleRepository: ScheduleRepository,
) : NoParamsSuspendableUseCase<Flow<AdditionSchedule?>>(coroutineContextProvider) {

    override suspend fun buildRequest(): Flow<AdditionSchedule?> {
        return scheduleRepository.getScheduleFLow()
    }
}