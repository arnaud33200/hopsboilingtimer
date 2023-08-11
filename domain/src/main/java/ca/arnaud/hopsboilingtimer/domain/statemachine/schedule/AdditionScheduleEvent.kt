package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule

import ca.arnaud.hopsboilingtimer.domain.statemachine.MachineEvent

sealed interface AdditionScheduleEvent : MachineEvent {

    object StartClick : AdditionScheduleEvent {
        override val id = "StartClick"
    }

    object CancelClick : AdditionScheduleEvent {
        override val id = "CancelClick"
    }

    object PauseClick : AdditionScheduleEvent {
        override val id = "PauseClick"
    }

    object ResumeClick : AdditionScheduleEvent {
        override val id = "ResumeClick"
    }

    object TimerEnd : AdditionScheduleEvent {
        override val id = "TimerEnd"
    }
}