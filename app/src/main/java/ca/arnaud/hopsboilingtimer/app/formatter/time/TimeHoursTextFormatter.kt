package ca.arnaud.hopsboilingtimer.app.formatter.time

import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.formatter.Formatter
import ca.arnaud.hopsboilingtimer.app.provider.StringProvider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TimeHoursTextFormatter @Inject constructor(
    private val stringProvider: StringProvider,
) : Formatter<LocalDateTime, String> {

    override fun format(input: LocalDateTime): String {
        return input.format(
            DateTimeFormatter.ofPattern(stringProvider.get(R.string.time_hours_minute_format_pattern))
        )
    }
}