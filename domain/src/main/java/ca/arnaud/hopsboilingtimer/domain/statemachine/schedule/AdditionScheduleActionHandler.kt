package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule

import ca.arnaud.hopsboilingtimer.domain.factory.AdditionScheduleFactory
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
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

        sealed class ResumeAction : AdditionScheduleActionError() {

            object MissingSchedule : ResumeAction()
            object AlreadyResumed : ResumeAction()
            object ExpiredSchedule : ResumeAction()
        }

        sealed class PauseAction : AdditionScheduleActionError() {

            object MissingSchedule : PauseAction()
        }
    }

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
        val schedule = scheduleRepository.getSchedule()
            ?: throw AdditionScheduleActionError.PauseAction.MissingSchedule

        scheduleRepository.setSchedule(
            schedule.copy(
                pauseTime = timeProvider.getNowLocalDateTime(),
            )
        )
        scheduleRepository.setNextAlert(null)
    }

    @Throws(AdditionScheduleActionError.ResumeAction::class)
    private suspend fun resumeSchedule(transition: AdditionScheduleTransition) {
        val schedule = scheduleRepository.getSchedule()
            ?: throw AdditionScheduleActionError.ResumeAction.MissingSchedule
        val pauseTime = schedule.pauseTime
            ?: throw AdditionScheduleActionError.ResumeAction.AlreadyResumed

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
            ?: throw AdditionScheduleActionError.ResumeAction.ExpiredSchedule

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