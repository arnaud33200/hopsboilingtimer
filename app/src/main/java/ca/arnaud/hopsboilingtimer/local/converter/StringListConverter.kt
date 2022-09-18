package ca.arnaud.hopsboilingtimer.local.converter

import androidx.room.TypeConverter
import java.time.Duration

class StringListConverter {

    companion object {
        const val LIST_STRING_SEPARATOR = ";"
    }

    @TypeConverter
    fun fromList(strings: List<String>?): String {
        return strings?.joinToString(separator = LIST_STRING_SEPARATOR) ?: ""
    }

    @TypeConverter
    fun toList(string: String?): List<String> {
        return string?.split(LIST_STRING_SEPARATOR) ?: emptyList()
    }
}