package ca.arnaud.hopsboilingtimer.domain.statemachine

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleState
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
            ScheduleState.Started,
            subject.transition(
                ScheduleState.Idle,
                AdditionScheduleEvent.TimerStart
            )!!.toState
        )
        assertEquals(
            ScheduleState.Started,
            subject.transition(
                ScheduleState.Canceled,
                AdditionScheduleEvent.TimerStart
            )!!.toState
        )
        assertEquals(
            ScheduleState.Started,
            subject.transition(
                ScheduleState.Stopped,
                AdditionScheduleEvent.TimerStart
            )!!.toState
        )
        assertEquals(
            ScheduleState.Canceled,
            subject.transition(
                ScheduleState.Started,
                AdditionScheduleEvent.Cancel
            )!!.toState
        )
        assertEquals(
            ScheduleState.Stopped,
            subject.transition(
                ScheduleState.Started,
                AdditionScheduleEvent.TimerEnd
            )!!.toState
        )
    }
}