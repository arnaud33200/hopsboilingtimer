package ca.arnaud.hopsboilingtimer.domain.usecase.schedulestate

import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleState
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsSuspendableUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class SubscribeScheduleState @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    @Named("InitScheduleState")
    private val initCompletion: CompletableDeferred<Unit>,
    private val scheduleStateRepository: ScheduleStateRepository,
) : NoParamsSuspendableUseCase<Flow<AdditionScheduleState>>(jobExecutorProvider) {

    override suspend fun buildRequest(): Flow<AdditionScheduleState> {
        initCompletion.await()
        return scheduleStateRepository.getScheduleStatusFlow()
    }
}