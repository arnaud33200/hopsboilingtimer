package ca.arnaud.hopsboilingtimer.app.formatter.time

import ca.arnaud.hopsboilingtimer.app.formatter.Formatter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TimeHoursTextFormatter @Inject constructor() : Formatter<LocalDateTime, String> {

    override fun format(input: LocalDateTime): String {
        return input.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }
}