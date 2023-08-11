package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule

import ca.arnaud.hopsboilingtimer.domain.factory.AdditionScheduleFactory
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.error.AdditionScheduleActionError
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.error.AdditionSchedulePauseActionError
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.error.AdditionScheduleResumeActionError
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.error.AdditionScheduleStartActionError
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import java.time.Duration
import javax.inject.Inject

class AdditionScheduleActionHandler @Inject constructor(
    private val timeProvider: TimeProvider,
    private val getAdditions: GetAdditions,
    private val additionScheduleFactory: AdditionScheduleFactory,
    private val scheduleRepository: ScheduleRepository,
) {

    @Throws(AdditionScheduleActionError::class)
    suspend fun handle(transition: AdditionScheduleTransition) {
        // TODO - convert this into action?
        //  each sub class would have just one function and unit test can be separated
        when (transition.toState) {
            AdditionScheduleState.Idle -> {} // No-op
            AdditionScheduleState.Started -> {
                when (transition.event) {
                    AdditionScheduleEvent.StartClick -> startSchedule(transition)
                    AdditionScheduleEvent.ResumeClick -> resumeSchedule(transition)
                    AdditionScheduleEvent.CancelClick,
                    AdditionScheduleEvent.PauseClick,
                    AdditionScheduleEvent.TimerEnd -> {
                    } // No-op
                }
            }

            AdditionScheduleState.Paused -> pauseSchedule(transition)
            AdditionScheduleState.Canceled,
            AdditionScheduleState.Finished -> {
                stopSchedule()
            }
        }
    }

    @Throws(AdditionScheduleActionError::class)
    private suspend fun startSchedule(transition: AdditionScheduleTransition) {
        val params = (transition.params as? AdditionScheduleParams.Start)?.scheduleOptions
            ?: throw AdditionScheduleStartActionError.MissingParams

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
        val schedule = scheduleRepository.getSchedule()
            ?: throw AdditionSchedulePauseActionError.MissingSchedule

        scheduleRepository.setSchedule(
            schedule.copy(
                pauseTime = timeProvider.getNowLocalDateTime(),
            )
        )
        scheduleRepository.setNextAlert(null)
    }

    @Throws(AdditionScheduleResumeActionError::class)
    private suspend fun resumeSchedule(transition: AdditionScheduleTransition) {
        val schedule = scheduleRepository.getSchedule()
            ?: throw AdditionScheduleResumeActionError.MissingSchedule
        val pauseTime = schedule.pauseTime
            ?: throw AdditionScheduleResumeActionError.AlreadyResumed

        val nowTime = timeProvider.getNowLocalDateTime()
        val delaySincePause = Duration.between(pauseTime, nowTime)

        val resumeSchedule = schedule.copy(
            startingTime = schedule.startingTime + delaySincePause,
            pauseTime = null,
            alerts = schedule.alerts.map { alert ->
                val triggerAtTime = alert.triggerAtTime + delaySincePause
                when (alert) {
                    is AdditionAlert.Start -> alert.copy(triggerAtTime = triggerAtTime)
                    is AdditionAlert.End -> alert.copy(triggerAtTime = triggerAtTime)
                    is AdditionAlert.Next -> alert.copy(triggerAtTime = triggerAtTime)
                }
            }
        )

        val nextAlert = resumeSchedule.getNextAlert(nowTime)
            ?: throw AdditionScheduleResumeActionError.ExpiredSchedule

        scheduleRepository.setSchedule(resumeSchedule)
        scheduleRepository.setNextAlert(nextAlert)
    }

    private suspend fun stopSchedule() {
        scheduleRepository.getSchedule()?.let { schedule ->
            scheduleRepository.deleteSchedule(schedule)
            scheduleRepository.setNextAlert(null)
        }
    }
}