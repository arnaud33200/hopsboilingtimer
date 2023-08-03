package ca.arnaud.hopsboilingtimer.domain.model.schedule

sealed interface ScheduleStatus {

    object Iddle: ScheduleStatus

    object Stopped: ScheduleStatus

    data class Started(
        val schedule: AdditionSchedule,
    ): ScheduleStatus

    object Canceled: ScheduleStatus
}

fun ScheduleStatus.getSchedule(): AdditionSchedule? {
    return when (this) {
        is ScheduleStatus.Started -> this.schedule

        ScheduleStatus.Iddle,
        ScheduleStatus.Canceled,
        ScheduleStatus.Stopped -> null
    }
}