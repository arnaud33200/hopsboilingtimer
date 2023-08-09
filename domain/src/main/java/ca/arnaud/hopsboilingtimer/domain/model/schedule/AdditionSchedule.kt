package ca.arnaud.hopsboilingtimer.domain.model.schedule

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import java.time.LocalDateTime

data class AdditionSchedule(
    val startingTime: LocalDateTime,
    val alerts: List<AdditionAlert>,
) {

    val id = "CURRENT_SCHEDULE" // Force to only have one schedule until we support it

    val endingTime = alerts.maxOf { alert -> alert.triggerAtTime }
}

fun AdditionSchedule.getNextAlert(time: LocalDateTime): AdditionAlert? {
    return alerts.firstOrNull { alert -> alert.triggerAtTime > time }
}

fun AdditionSchedule?.isValid(nowTime: LocalDateTime): Boolean {
    return when {
        this == null -> false
        endingTime <= nowTime -> false
        else -> true
    }
}