package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule

import ca.arnaud.hopsboilingtimer.domain.factory.AdditionScheduleFactory
import ca.arnaud.hopsboilingtimer.domain.model.schedule.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.Transition
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import java.time.Duration
import javax.inject.Inject

class AdditionScheduleEventHandler @Inject constructor(
    private val timeProvider: TimeProvider,
    private val getAdditions: GetAdditions,
    private val additionScheduleFactory: AdditionScheduleFactory,
    private val scheduleRepository: ScheduleRepository,
) {

    suspend fun handle(
        transition: Transition<AdditionScheduleState, AdditionScheduleEvent, AdditionScheduleParams>
    ) {
        when (transition.toState) {
            AdditionScheduleState.Idle -> {} // No-op
            AdditionScheduleState.Started -> startSchedule(transition)
            AdditionScheduleState.Canceled,
            AdditionScheduleState.Stopped -> {
                stopSchedule()
            }
        }
    }

    private suspend fun startSchedule(
        transition: Transition<AdditionScheduleState, AdditionScheduleEvent, AdditionScheduleParams>
    ) {
        // TODO throw an error
        val params = (transition.params as? AdditionScheduleParams.Start)?.scheduleOptions ?: return

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

    private suspend fun stopSchedule() {
        scheduleRepository.getSchedule()?.let { schedule ->
            scheduleRepository.deleteSchedule(schedule)
            scheduleRepository.setNextAlert(null)
        }
    }
}