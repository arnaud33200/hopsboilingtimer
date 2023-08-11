package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.error

sealed class AdditionScheduleStartActionError : AdditionScheduleActionError() {

    object MissingParams : AdditionScheduleActionError()
}