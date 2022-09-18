package ca.arnaud.hopsboilingtimer.domain.model

import java.time.Duration

data class AdditionAlert(
    val triggerAtTime: Long,
    val additions: List<Addition>
)

fun List<AdditionAlert>.getMinInterval(): Duration {
    return Duration.ZERO
//    if (size == 1) {
//        return first().countDown
//    }
//    val sortedAlerts = sortedBy { it.countDown }
//    return sortedAlerts.mapIndexedNotNull { index, additionAlert ->
//        sortedAlerts.getOrNull(index+1)?.let { nextAddition ->
//            nextAddition.countDown - additionAlert.countDown
//        }
//    }.minOrNull() ?: Duration.ZERO
}

