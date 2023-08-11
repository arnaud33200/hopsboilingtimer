package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule

import ca.arnaud.hopsboilingtimer.domain.factory.AdditionScheduleFactory
import ca.arnaud.hopsboilingtimer.domain.model.schedule.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import java.time.Duration
import javax.inject.Inject

class AdditionScheduleActionHandler @Inject constructor(
    private val timeProvider: TimeProvider,
    private val getAdditions: GetAdditions,
    private val additionScheduleFactory: AdditionScheduleFactory,
    private val scheduleRepository: ScheduleRepository,
) {

    sealed class AdditionScheduleActionError : Throwable() {

        object StartScheduleMissingParams : AdditionScheduleActionError()
    }

    @Throws(AdditionScheduleActionError::class)
    suspend fun handle(transition: AdditionScheduleTransition) {
        when (transition.toState) {
            AdditionScheduleState.Idle -> {} // No-op
            AdditionScheduleState.Started -> startSchedule(transition)
            AdditionScheduleState.Paused -> pauseSchedule(transition)
            AdditionScheduleState.Canceled,
            AdditionScheduleState.Stopped -> {
                stopSchedule()
            }
        }
    }

    @Throws(AdditionScheduleActionError::class)
    private suspend fun startSchedule(transition: AdditionScheduleTransition) {
        val params = (transition.params as? AdditionScheduleParams.Start)?.scheduleOptions
            ?: throw AdditionScheduleActionError.StartScheduleMissingParams

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
        scheduleRepository.setSchedule(schedule)

        val nextAlert = schedule.getNextAlert(startTime)
        scheduleRepository.setNextAlert(nextAlert)
    }

    private suspend fun pauseSchedule(transition: AdditionScheduleTransition) {
        // TODO - pause action (#14)
    }

    private suspend fun stopSchedule() {
        scheduleRepository.getSchedule()?.let { schedule ->
            scheduleRepository.deleteSchedule(schedule)
            scheduleRepository.setNextAlert(null)
        }
    }
}