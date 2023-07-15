package ca.arnaud.hopsboilingtimer.app.feature.common.mapper

import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

// TODO - rename it formatter
class RemainingTimeTextMapper @Inject constructor() : DataMapper<Duration, String> {

    override fun mapTo(input: Duration): String {
        val minutes = input.inWholeMinutes
        val minutesText = addZeroIfNeeded("$minutes")
        val seconds = input.inWholeSeconds - minutes.minutes.inWholeSeconds
        val secondsText = addZeroIfNeeded("$seconds")
        return "$minutesText:$secondsText"
    }

    private fun addZeroIfNeeded(text: String): String {
        return when (text.length) {
            0 -> "00"
            1 -> "0$text"
            else -> text
        }
    }
}