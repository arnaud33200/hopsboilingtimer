package ca.arnaud.hopsboilingtimer.domain.statemachine.scheduleactionhandler

import ca.arnaud.hopsboilingtimer.commontest.fake.FakeTimeProvider
import ca.arnaud.hopsboilingtimer.commontest.model.additionAlertStartDefault
import ca.arnaud.hopsboilingtimer.commontest.model.additionScheduleDefault
import ca.arnaud.hopsboilingtimer.domain.extension.toEntry
import ca.arnaud.hopsboilingtimer.domain.factory.AdditionScheduleFactory
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleActionHandler
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleEvent
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleState
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleTransition
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.error.AdditionScheduleResumeActionError
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime

class ScheduleAdditionScheduleResumeActionErrorHandlerTest {

    private lateinit var subject: AdditionScheduleActionHandler

    private val timeProvider = FakeTimeProvider()
    private val getAdditions: GetAdditions = mockk()
    private val additionScheduleFactory: AdditionScheduleFactory = mockk()
    private val scheduleRepository: ScheduleRepository = mockk()

    @Before
    fun setup() {
        subject = AdditionScheduleActionHandler(
            timeProvider = timeProvider,
            getAdditions = getAdditions,
            additionScheduleFactory = additionScheduleFactory,
            scheduleRepository = scheduleRepository,
        )
    }

    @Test(expected = AdditionScheduleResumeActionError.MissingSchedule::class)
    fun `GIVEN null schedule and resume transition WHEN handling VERIFY MissingSchedule error`() =
        runTest {
            val transition = object : AdditionScheduleTransition {
                override val fromState = AdditionScheduleState.Paused
                override val toState = AdditionScheduleState.Started
                override val event = AdditionScheduleEvent.ResumeClick
                override val params = null
            }
            coEvery { scheduleRepository.getSchedule() } returns null

            subject.handle(transition)
        }

    @Test(expected = AdditionScheduleResumeActionError.AlreadyResumed::class)
    fun `GIVEN schedule without pause time and resume transition WHEN handling VERIFY AlreadyResumed error`() =
        runTest {
            val transition = object : AdditionScheduleTransition {
                override val fromState = AdditionScheduleState.Paused
                override val toState = AdditionScheduleState.Started
                override val event = AdditionScheduleEvent.ResumeClick
                override val params = null
            }

            coEvery { scheduleRepository.getSchedule() } returns additionScheduleDefault.copy(
                pauseTime = null,
            )

            subject.handle(transition)
        }

    @Test(expected = AdditionScheduleResumeActionError.ExpiredSchedule::class)
    fun `GIVEN schedule pause at end time and resume transition WHEN handling VERIFY ExpiredSchedule error`() =
        runTest {
            val transition = object : AdditionScheduleTransition {
                override val fromState = AdditionScheduleState.Paused
                override val toState = AdditionScheduleState.Started
                override val event = AdditionScheduleEvent.ResumeClick
                override val params = null
            }

            val startTime = LocalDateTime.of(2023, 8, 11, 10, 18)
            val endTime = startTime + Duration.ofMinutes(60)

            val alertData = listOf(
                ("60min" to startTime).toEntry(),
                ("50min" to startTime + Duration.ofMinutes(10)).toEntry(),
                ("5min" to startTime + Duration.ofMinutes(55)).toEntry(),
                ("end" to endTime).toEntry(),
            )

            val pauseTime = endTime
            val schedule = alertData.toSchedule(startTime, pauseTime)

            val resumeTime = endTime
            timeProvider.now = resumeTime
            coEvery { scheduleRepository.getSchedule() } returns schedule

            subject.handle(transition)
        }

    @Test
    fun `GIVEN valid schedule and resume transition WHEN handling VERIFY schedule and next alert set`() =
        runTest {
            val transition = object : AdditionScheduleTransition {
                override val fromState = AdditionScheduleState.Paused
                override val toState = AdditionScheduleState.Started
                override val event = AdditionScheduleEvent.ResumeClick
                override val params = null
            }

            val startTime = LocalDateTime.of(2023, 8, 11, 10, 18)
            val endTime = startTime + Duration.ofMinutes(60)
            val pauseTime = startTime + Duration.ofMinutes(5)

            val alertData = listOf(
                ("60min" to startTime).toEntry(),
                ("50min" to startTime + Duration.ofMinutes(10)).toEntry(),
                ("5min" to startTime + Duration.ofMinutes(55)).toEntry(),
                ("end" to endTime).toEntry(),
            )
            val schedule = alertData.toSchedule(startTime, pauseTime)

            val resumeTime = pauseTime + Duration.ofMinutes(10)
            timeProvider.now = resumeTime

            coEvery { scheduleRepository.getSchedule() } returns schedule

            val expectedDelay = Duration.ofMinutes(10)
            val expectedAlerts = schedule.alerts.map { alert ->
                val newTime = alert.triggerAtTime + expectedDelay
                when (alert) {
                    is AdditionAlert.Start -> alert.copy(triggerAtTime = newTime)
                    is AdditionAlert.End -> alert.copy(triggerAtTime = newTime)
                    is AdditionAlert.Next -> alert.copy(triggerAtTime = newTime)
                }
            }
            val expectedSchedule = schedule.copy(
                startingTime = startTime + expectedDelay,
                alerts = expectedAlerts,
                pauseTime = null,
            )

            coEvery { scheduleRepository.setSchedule(any()) } just Runs
            coEvery { scheduleRepository.setNextAlert(any()) } just Runs

            subject.handle(transition)

            coVerify {
                scheduleRepository.setSchedule(expectedSchedule)
                scheduleRepository.setNextAlert(expectedSchedule.alerts[1])
            }
        }

    private fun List<Map.Entry<String, LocalDateTime>>.toSchedule(
        startTime: LocalDateTime,
        pauseTime: LocalDateTime,
    ): AdditionSchedule {
        return additionScheduleDefault.copy(
            startingTime = startTime,
            pauseTime = pauseTime,
            alerts = this.map { entry ->
                additionAlertStartDefault.copy(id = entry.key, triggerAtTime = entry.value)
            }
        )
    }
}