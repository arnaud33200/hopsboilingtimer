package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule

import ca.arnaud.hopsboilingtimer.domain.statemachine.MachineEvent

sealed interface AdditionScheduleEvent : MachineEvent {

    object TimerStart : AdditionScheduleEvent {
        override val id = "TimerStart"
    }

    object Cancel : AdditionScheduleEvent {
        override val id = "Cancel"
    }

    object TimerEnd : AdditionScheduleEvent {
        override val id = "TimerEnd"
    }
}