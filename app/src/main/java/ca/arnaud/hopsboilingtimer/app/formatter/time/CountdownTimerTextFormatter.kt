package ca.arnaud.hopsboilingtimer.app.formatter.time

import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.extension.toPositiveDuration
import ca.arnaud.hopsboilingtimer.app.formatter.Formatter
import ca.arnaud.hopsboilingtimer.app.provider.StringProvider
import java.time.Duration
import javax.inject.Inject

class CountdownTimerTextFormatter @Inject constructor(
    private val stringProvider: StringProvider,
) : Formatter<Duration, String> {

    override fun format(input: Duration): String {
        val positiveDuration = input.toPositiveDuration()
        val seconds = positiveDuration.toSecondsPart()
        val minutes = Duration.ofSeconds(positiveDuration.toSeconds() - seconds).toMinutes()
        val minutesText = addZeroIfNeeded("$minutes")
        val secondsText = addZeroIfNeeded("$seconds")
        val negativeSign = if (input.isNegative) "-" else ""
        return negativeSign + stringProvider.get(
            R.string.time_countdown_minutes_seconds_pattern,
            minutesText,
            secondsText,
        )
    }

    private fun addZeroIfNeeded(text: String): String {
        return when (text.length) {
            0 -> "00"
            1 -> "0$text"
            else -> text
        }
    }
}