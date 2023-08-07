package ca.arnaud.hopsboilingtimer.domain.model.schedule

sealed interface ScheduleStatus {

    object Iddle: ScheduleStatus

    object Stopped: ScheduleStatus

    object Started : ScheduleStatus

    object Canceled: ScheduleStatus
}