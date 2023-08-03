package ca.arnaud.hopsboilingtimer.domain.usecase.schedule

import ca.arnaud.hopsboilingtimer.domain.factory.AdditionScheduleFactory
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleOptions
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import java.time.Duration
import javax.inject.Inject

class StartAdditionSchedule @Inject constructor(
    jobExecutorProvider: JobExecutorProvider,
    private val timeProvider: TimeProvider,
    private val getAdditions: GetAdditions,
    private val additionScheduleFactory: AdditionScheduleFactory,
    private val scheduleRepository: ScheduleRepository,
) : SuspendableUseCase<ScheduleOptions, Unit>(jobExecutorProvider) {

    override suspend fun buildRequest(params: ScheduleOptions) {
        val additions = getAdditions.execute(Unit).getOrDefault(emptyList())

        val maxDuration = additions.maxOfOrNull { it.duration } ?: Duration.ZERO
        val delay = when {
            params.delay == null -> Duration.ZERO
            params.delay > maxDuration -> maxDuration
            params.delay < Duration.ZERO -> Duration.ZERO
            else -> params.delay
        }
        val startTime = timeProvider.getNowLocalDateTime() - delay

        val schedule = additionScheduleFactory.create(additions, startTime)
        scheduleRepository.setAdditionScheduleStatus(ScheduleStatus.Started(schedule))
    }
}