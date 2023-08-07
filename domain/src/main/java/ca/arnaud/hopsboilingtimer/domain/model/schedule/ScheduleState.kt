package ca.arnaud.hopsboilingtimer.domain.model.schedule

import ca.arnaud.hopsboilingtimer.domain.statemachine.MachineState

sealed interface ScheduleState : MachineState {

    object Idle : ScheduleState {
        override val id = "Iddle"
    }

    object Stopped : ScheduleState {
        override val id = "Stopped"
    }

    object Started : ScheduleState {
        override val id = "Started"
    }

    object Canceled : ScheduleState {
        override val id = "Canceled"
    }
}