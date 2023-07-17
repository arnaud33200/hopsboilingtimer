package ca.arnaud.hopsboilingtimer.app.extension

import java.time.Duration

fun Duration.toPositiveDuration(): Duration {
    return when {
        isNegative -> Duration.ofMillis(kotlin.math.abs(toMillis()))
        else -> this
    }
}