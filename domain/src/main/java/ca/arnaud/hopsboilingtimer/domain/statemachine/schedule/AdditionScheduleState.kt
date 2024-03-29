package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule

import ca.arnaud.hopsboilingtimer.domain.statemachine.MachineState

sealed interface AdditionScheduleState : MachineState {

    object Idle : AdditionScheduleState {
        override val id = "Idle"
    }

    object Finished : AdditionScheduleState {
        override val id = "Stopped"
    }

    object Started : AdditionScheduleState {
        override val id = "Started"
    }

    object Paused : AdditionScheduleState {
        override val id = "Paused"
    }

    object Canceled : AdditionScheduleState {
        override val id = "Canceled"
    }
}