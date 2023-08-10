package ca.arnaud.hopsboilingtimer.domain.usecase

import ca.arnaud.hopsboilingtimer.domain.TestCoroutineContextProvider
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleStateRepository
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleState
import ca.arnaud.hopsboilingtimer.domain.usecase.schedulestate.SubscribeScheduleState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class SubscribeScheduleStateTest {

    private lateinit var subject: SubscribeScheduleState

    private val coroutineContextProvider = TestCoroutineContextProvider()
    private val initCompletion: CompletableDeferred<Unit> = CompletableDeferred()
    private val scheduleStateRepository: ScheduleStateRepository = mockk()

    @Before
    fun setup() {
        subject = SubscribeScheduleState(
            coroutineContextProvider = coroutineContextProvider,
            initCompletion = initCompletion,
            scheduleStateRepository = scheduleStateRepository,
        )
    }

    @Test
    fun `GIVEN incomplete init completion WHEN executing VERIFY still waiting`() =
        coroutineContextProvider.runTest(timeout = 5.seconds) {
            val job = launch { subject.execute() }
            advanceTimeBy(1.seconds)
            assert(job.isActive)
            job.cancelAndJoin()
        }

    @Test
    fun `GIVEN complete init completion WHEN executing VERIFY still waiting`() =
        coroutineContextProvider.runTest(timeout = 5.seconds) {
            coEvery { scheduleStateRepository.getScheduleStatusFlow() } returns mockk()

            val job = launch { subject.execute() }
            initCompletion.complete(Unit)
            advanceTimeBy(1.seconds)
            assert(job.isCompleted)
        }

    @Test
    fun `GIVEN flow from repo WHEN executing VERIFY flow return`() =
        coroutineContextProvider.runTest {
            val flow = mockk<Flow<AdditionScheduleState>>()
            coEvery { scheduleStateRepository.getScheduleStatusFlow() } returns flow

            initCompletion.complete(Unit)
            assertEquals(flow, subject.execute())
        }
}