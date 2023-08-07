package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule

import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleOptions
import ca.arnaud.hopsboilingtimer.domain.statemachine.MachineParams

sealed interface AdditionScheduleParams : MachineParams {

    data class Start(val scheduleOptions: ScheduleOptions) : AdditionScheduleParams
}