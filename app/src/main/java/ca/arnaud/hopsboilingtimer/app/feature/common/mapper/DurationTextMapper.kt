package ca.arnaud.hopsboilingtimer.app.feature.common.mapper

import ca.arnaud.hopsboilingtimer.domain.common.DataMapper
import java.time.Duration
import javax.inject.Inject

// TODO - rename it formatter
class DurationTextMapper @Inject constructor() : DataMapper<Duration, String> {

    override fun mapTo(input: Duration): String {
        return when {
            input.toMinutes() > 0 -> "${input.toMinutes()} min"
            else -> "${input.toMillis() / 1000} sec"
        }
    }
}