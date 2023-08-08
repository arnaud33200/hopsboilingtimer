package ca.arnaud.hopsboilingtimer.app.formatter.time

import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.extension.toPositiveDuration
import ca.arnaud.hopsboilingtimer.app.formatter.Formatter
import ca.arnaud.hopsboilingtimer.app.provider.StringProvider
import java.time.Duration
import javax.inject.Inject

class DurationTextFormatter @Inject constructor(
    private val stringProvider: StringProvider,
) : Formatter<Duration, String> {

    override fun format(input: Duration): String {
        return when {
            input.toPositiveDuration().toMinutes() > 0 -> {
                stringProvider.get(
                    R.string.time_duration_minutes_pattern,
                    "${input.toMinutes()}"
                )
            }

            else -> {
                stringProvider.get(
                    R.string.time_duration_seconds_pattern,
                    "${input.toSeconds()}"
                )
            }
        }
    }
}