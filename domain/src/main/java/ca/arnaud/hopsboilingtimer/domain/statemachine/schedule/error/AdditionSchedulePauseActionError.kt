package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.error

sealed class AdditionSchedulePauseActionError : AdditionScheduleActionError() {

    object MissingSchedule : AdditionSchedulePauseActionError()
}