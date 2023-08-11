package ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.error

sealed class AdditionScheduleResumeActionError : AdditionScheduleActionError() {

    object MissingSchedule : AdditionScheduleResumeActionError()
    object AlreadyResumed : AdditionScheduleResumeActionError()
    object ExpiredSchedule : AdditionScheduleResumeActionError()
}