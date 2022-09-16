package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.factory.AdditionScheduleFactory
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsSuspendableUseCase
import javax.inject.Inject

class SetAdditionAlertReceived @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val additionScheduleFactory: AdditionScheduleFactory,
    private val scheduleRepository: ScheduleRepository,
) : NoParamsSuspendableUseCase<Unit>(jobExecutorProvider) {

    override suspend fun buildRequest() {

    }
}