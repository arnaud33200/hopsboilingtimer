package ca.arnaud.hopsboilingtimer.domain.statemachine

import ca.arnaud.hopsboilingtimer.domain.factory.AdditionScheduleFactory
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
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
        transition: Transition<AdditionScheduleState, AdditionScheduleEvent>
    ) {
        when (transition.toState) {
            AdditionScheduleState.Iddle -> {} // No-op
            AdditionScheduleState.Going -> startSchedule(transition.event)
            AdditionScheduleState.Canceled,
            AdditionScheduleState.Stopped -> {
                stopSchedule()
            }
        }
    }

    private suspend fun startSchedule(event: AdditionScheduleEvent) {
        val params = (event as? AdditionScheduleEvent.TimerStart)?.params ?: return

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
        scheduleRepository.setAdditionSchedule(schedule)
    }

    private suspend fun stopSchedule() {
        scheduleRepository.setAdditionSchedule(null)
    }
}