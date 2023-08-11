package ca.arnaud.hopsboilingtimer.domain.statemachine.scheduleactionhandler

import ca.arnaud.hopsboilingtimer.commontest.fake.FakeTimeProvider
import ca.arnaud.hopsboilingtimer.commontest.model.additionScheduleDefault
import ca.arnaud.hopsboilingtimer.domain.factory.AdditionScheduleFactory
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleActionHandler
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleEvent
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleState
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleTransition
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.error.AdditionSchedulePauseActionError
import ca.arnaud.hopsboilingtimer.domain.usecase.addition.GetAdditions
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class SchedulePauseActionHandlerTest {

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

    @Test(expected = AdditionSchedulePauseActionError.MissingSchedule::class)
    fun `GIVEN null schedule and pause transition WHEN handling VERIFY MissingSchedule error`() =
        runTest {
            timeProvider.now = LocalDateTime.of(2023, 8, 11, 10, 18)
            val transition = object : AdditionScheduleTransition {
                override val fromState = AdditionScheduleState.Started
                override val toState = AdditionScheduleState.Paused
                override val event = AdditionScheduleEvent.PauseClick
                override val params = null
            }

            coEvery { scheduleRepository.getSchedule() } returns null

            subject.handle(transition)
        }

    @Test
    fun `GIVEN valid schedule and pause transition WHEN handling VERIFY schedule and next alert set`() =
        runTest {
            timeProvider.now = LocalDateTime.of(2023, 8, 11, 10, 18)
            val transition = object : AdditionScheduleTransition {
                override val fromState = AdditionScheduleState.Started
                override val toState = AdditionScheduleState.Paused
                override val event = AdditionScheduleEvent.PauseClick
                override val params = null
            }
            val schedule = additionScheduleDefault.copy()

            coEvery { scheduleRepository.getSchedule() } returns schedule

            coEvery { scheduleRepository.setSchedule(any()) } just Runs
            coEvery { scheduleRepository.setNextAlert(any()) } just Runs

            subject.handle(transition)

            coVerify {
                scheduleRepository.setSchedule(schedule.copy(pauseTime = timeProvider.now))
                scheduleRepository.setNextAlert(null)
            }
        }
}