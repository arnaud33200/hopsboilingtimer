package ca.arnaud.hopsboilingtimer.app.formatter.time

import ca.arnaud.hopsboilingtimer.app.extension.toPositiveDuration
import ca.arnaud.hopsboilingtimer.app.formatter.Formatter
import java.time.Duration
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class RemainingTimeTextFormatter @Inject constructor() : Formatter<Duration, String> {

    override fun format(input: Duration): String {
        val positiveDuration = input.toPositiveDuration()
        val seconds = positiveDuration.toSecondsPart()
        val minutes = Duration.ofSeconds(positiveDuration.toSeconds() - seconds).toMinutes()
        val minutesText = addZeroIfNeeded("$minutes")
        val secondsText = addZeroIfNeeded("$seconds")
        val negativeSign = if (input.isNegative) "-" else ""
        return "$negativeSign$minutesText:$secondsText"
    }

    private fun addZeroIfNeeded(text: String): String {
        return when (text.length) {
            0 -> "00"
            1 -> "0$text"
            else -> text
        }
    }
}