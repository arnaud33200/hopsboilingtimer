package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleState
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
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
) : NoParamsSuspendableUseCase<Flow<ScheduleState>>(jobExecutorProvider) {

    override suspend fun buildRequest(): Flow<ScheduleState> {
        initCompletion.await()
        return scheduleStateRepository.getScheduleStatusFlow()
    }
}