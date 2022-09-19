package ca.arnaud.hopsboilingtimer.domain.model

data class AdditionSchedule(
    val startingTime: Long,
    val alerts: List<AdditionAlert>,
) {
    val id = "CURRENT_SCHEDULE" // Force to only have one schedule until we support it
}

fun AdditionSchedule.getNextAlert(time: Long): AdditionAlert? {
    return alerts.firstOrNull { alert -> alert.triggerAtTime > time }
}