package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.factory.AdditionScheduleFactory
import ca.arnaud.hopsboilingtimer.domain.model.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.GetAdditions
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsSuspendableUseCase
import javax.inject.Inject

class StartAdditionSchedule @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val timeProvider: TimeProvider,
    private val getAdditions: GetAdditions,
    private val additionScheduleFactory: AdditionScheduleFactory,
    private val scheduleRepository: ScheduleRepository
) : NoParamsSuspendableUseCase<Unit>(jobExecutorProvider) {

    // TODO - can put a starting time as param or skip XX minutes

    override suspend fun buildRequest() {
        val additions = getAdditions.execute(Unit).getOrDefault(emptyList())
        val schedule = additionScheduleFactory.create(additions, timeProvider.getNowTimeMillis())
        scheduleRepository.setAdditionSchedule(schedule)
    }
}