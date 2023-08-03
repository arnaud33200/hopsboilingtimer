package ca.arnaud.hopsboilingtimer.local.converter

import androidx.room.TypeConverter
import java.time.Duration

class DurationConverter {

    @TypeConverter
    fun fromDuration(duration: Duration?): Long {
        return duration?.toMillis() ?: 0L
    }

    @TypeConverter
    fun toDuration(long: Long?): Duration {
        return Duration.ofMillis(long ?: 0L)
    }
}