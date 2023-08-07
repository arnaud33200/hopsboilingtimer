package ca.arnaud.hopsboilingtimer.domain.statemachine

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AdditionScheduleStateMachineTest {

    private lateinit var subject: AdditionScheduleStateMachine

    @Before
    fun setup() {
        subject = AdditionScheduleStateMachine()

        println(subject.toString())
    }

    @Test
    fun `GIVEN different possible state & event combination WHEN doing all transition VERIFY correct states`() {
        assertEquals(
            AdditionScheduleState.Started,
            subject.transition(
                AdditionScheduleState.Idle,
                AdditionScheduleEvent.TimerStart
            )!!.toState
        )
        assertEquals(
            AdditionScheduleState.Started,
            subject.transition(
                AdditionScheduleState.Canceled,
                AdditionScheduleEvent.TimerStart
            )!!.toState
        )
        assertEquals(
            AdditionScheduleState.Started,
            subject.transition(
                AdditionScheduleState.Stopped,
                AdditionScheduleEvent.TimerStart
            )!!.toState
        )
        assertEquals(
            AdditionScheduleState.Canceled,
            subject.transition(
                AdditionScheduleState.Started,
                AdditionScheduleEvent.Cancel
            )!!.toState
        )
        assertEquals(
            AdditionScheduleState.Stopped,
            subject.transition(
                AdditionScheduleState.Started,
                AdditionScheduleEvent.TimerEnd
            )!!.toState
        )
    }
}