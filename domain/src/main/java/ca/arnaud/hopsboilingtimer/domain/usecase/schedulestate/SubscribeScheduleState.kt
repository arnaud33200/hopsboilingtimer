package ca.arnaud.hopsboilingtimer.domain.usecase.schedulestate

import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleState
import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsSuspendableUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class SubscribeScheduleState @Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    @Named("InitScheduleState")
    private val initCompletion: CompletableDeferred<Unit>,
    private val scheduleStateRepository: ScheduleStateRepository,
) : NoParamsSuspendableUseCase<Flow<AdditionScheduleState>>(coroutineContextProvider) {

    override suspend fun buildRequest(): Flow<AdditionScheduleState> {
        initCompletion.await()
        return scheduleStateRepository.getScheduleStatusFlow()
    }
}