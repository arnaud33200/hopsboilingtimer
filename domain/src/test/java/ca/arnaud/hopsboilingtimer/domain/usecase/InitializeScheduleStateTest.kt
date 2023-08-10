package ca.arnaud.hopsboilingtimer.domain.usecase

import ca.arnaud.hopsboilingtimer.commontest.fake.FakeTimeProvider
import ca.arnaud.hopsboilingtimer.commontest.model.additionAlertEndDefault
import ca.arnaud.hopsboilingtimer.commontest.model.additionScheduleDefault
import ca.arnaud.hopsboilingtimer.domain.TestCoroutineContextProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleState
import ca.arnaud.hopsboilingtimer.domain.usecase.schedulestate.InitializeScheduleState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime

class InitializeScheduleStateTest {

    private lateinit var subject: InitializeScheduleState

    private val coroutineContextProvider = TestCoroutineContextProvider()
    private val initCompletion: CompletableDeferred<Unit> = CompletableDeferred()
    private val scheduleRepository: ScheduleRepository = mockk()
    private val scheduleStateRepository: ScheduleStateRepository = mockk()
    private val timeProvider = FakeTimeProvider()

    @Before
    fun setup() {
        coEvery { scheduleRepository.getSchedule() } returns null
        coEvery { scheduleStateRepository.setScheduleStatus(any()) } just Runs

        subject = InitializeScheduleState(
            coroutineContextProvider = coroutineContextProvider,
            initCompletion = initCompletion,
            scheduleRepository = scheduleRepository,
            scheduleStateRepository = scheduleStateRepository,
            timeProvider = timeProvider,
        )
    }

    @Test
    fun `GIVEN completable differed WHEN executing VERIFY completion finish`() =
        coroutineContextProvider.runTest {
            assert(!initCompletion.isCompleted)
            subject.execute(Unit)
            assert(initCompletion.isCompleted)
        }

    @Test
    fun `GIVEN invalid schedule from repo WHEN executing VERIFY delete called`() =
        coroutineContextProvider.runTest {
            timeProvider.now = LocalDateTime.of(2023, 8, 9, 6, 30)
            val schedule = additionScheduleDefault.copy(
                alerts = listOf(
                    additionAlertEndDefault.copy(
                        triggerAtTime = timeProvider.now - Duration.ofMinutes(5)
                    )
                )
            )
            coEvery { scheduleRepository.getSchedule() } returns schedule
            coEvery { scheduleRepository.deleteSchedule(schedule) } just Runs

            subject.execute(Unit)
            coVerify(exactly = 1) {
                scheduleRepository.deleteSchedule(schedule)
            }
        }

    @Test
    fun `GIVEN null schedule from repo WHEN executing VERIFY state idle`() =
        coroutineContextProvider.runTest {
            coEvery { scheduleRepository.getSchedule() } returns null

            subject.execute(Unit)
            coVerify {
                scheduleStateRepository.setScheduleStatus(AdditionScheduleState.Idle)
            }
        }

    @Test
    fun `GIVEN schedule without alerts from repo WHEN executing VERIFY state idle`() =
        coroutineContextProvider.runTest {
            coEvery { scheduleRepository.getSchedule() } returns additionScheduleDefault.copy(
                alerts = emptyList()
            )

            coEvery { scheduleRepository.deleteSchedule(any()) } just Runs

            subject.execute(Unit)
            coVerify {
                scheduleStateRepository.setScheduleStatus(AdditionScheduleState.Idle)
            }
        }

    @Test
    fun `GIVEN valid schedule from repo WHEN executing VERIFY state started`() =
        coroutineContextProvider.runTest {
            timeProvider.now = LocalDateTime.of(2023, 8, 9, 6, 30)
            coEvery { scheduleRepository.getSchedule() } returns additionScheduleDefault.copy(
                startingTime = timeProvider.now,
                alerts = listOf(
                    additionAlertEndDefault.copy(
                        triggerAtTime = timeProvider.now + Duration.ofHours(1)
                    )
                )
            )

            coEvery { scheduleRepository.setNextAlert(any()) } just Runs

            subject.execute(Unit)
            coVerify {
                scheduleStateRepository.setScheduleStatus(AdditionScheduleState.Started)
            }
        }

    @Test
    fun `GIVEN schedule with next alert from repo WHEN executing VERIFY next alert set in repo`() =
        coroutineContextProvider.runTest {
            timeProvider.now = LocalDateTime.of(2023, 8, 9, 6, 30)
            val alert = additionAlertEndDefault.copy(
                triggerAtTime = timeProvider.now + Duration.ofHours(1)
            )
            coEvery { scheduleRepository.getSchedule() } returns additionScheduleDefault.copy(
                startingTime = timeProvider.now,
                alerts = listOf(alert)
            )

            coEvery { scheduleRepository.setNextAlert(alert) } just Runs

            subject.execute(Unit)
            coVerify(exactly = 1) {
                scheduleRepository.setNextAlert(alert)
            }
        }
}