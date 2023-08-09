package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsSuspendableUseCase
import javax.inject.Inject

class GetAdditionSchedule @Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    private val scheduleRepository: ScheduleRepository,
) : NoParamsSuspendableUseCase<AdditionSchedule?>(coroutineContextProvider) {

    override suspend fun buildRequest(): AdditionSchedule? {
        return scheduleRepository.getSchedule()
    }

}