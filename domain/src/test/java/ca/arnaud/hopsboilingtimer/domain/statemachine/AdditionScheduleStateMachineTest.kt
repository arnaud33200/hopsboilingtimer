package ca.arnaud.hopsboilingtimer.domain.statemachine

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleOptions
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Duration

class AdditionScheduleStateMachineTest {

    private lateinit var subject: AdditionScheduleStateMachine

    @Before
    fun setup() {
        subject = AdditionScheduleStateMachine()

        println(subject.toString())
    }

    @Test
    fun `GIVEN different time start event WHEN doing all transition VERIFY correct states`() {
        val timerStart = AdditionScheduleEvent.TimerStart(
            params = ScheduleOptions(delay = Duration.ofMillis(1))
        )
        val timerStartZeroDelay = AdditionScheduleEvent.TimerStart(
            params = ScheduleOptions(delay = Duration.ZERO)
        )
        val timerStartSecondsDelay = AdditionScheduleEvent.TimerStart(
            params = ScheduleOptions(delay = Duration.ofSeconds(10))
        )
        assertEquals(
            AdditionScheduleState.Going,
            subject.transition(AdditionScheduleState.Iddle, timerStart)!!.toState
        )
        assertEquals(
            AdditionScheduleState.Going,
            subject.transition(AdditionScheduleState.Canceled, timerStartZeroDelay)!!.toState
        )
        assertEquals(
            AdditionScheduleState.Going,
            subject.transition(AdditionScheduleState.Stopped, timerStartSecondsDelay)!!.toState
        )
        assertEquals(
            AdditionScheduleState.Canceled,
            subject.transition(AdditionScheduleState.Going, AdditionScheduleEvent.Cancel)!!.toState
        )
        assertEquals(
            AdditionScheduleState.Stopped,
            subject.transition(
                AdditionScheduleState.Going,
                AdditionScheduleEvent.TimerEnd
            )!!.toState
        )
    }
}