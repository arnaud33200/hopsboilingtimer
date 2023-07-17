package ca.arnaud.hopsboilingtimer.app.formatter.time

import ca.arnaud.hopsboilingtimer.app.extension.toPositiveDuration
import ca.arnaud.hopsboilingtimer.app.formatter.Formatter
import java.time.Duration
import javax.inject.Inject

class DurationTextFormatter @Inject constructor() : Formatter<Duration, String> {

    override fun format(input: Duration): String {
        return when {
            input.toPositiveDuration().toMinutes() > 0 -> "${input.toMinutes()} min"
            else -> "${input.toSeconds()} sec"
        }
    }
}