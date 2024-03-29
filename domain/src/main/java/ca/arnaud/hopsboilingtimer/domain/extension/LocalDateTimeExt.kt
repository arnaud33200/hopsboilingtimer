package ca.arnaud.hopsboilingtimer.domain.extension

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun LocalDateTime.toEpochMillis(zoneId: ZoneId): Long {
    return atZone(zoneId).toInstant().toEpochMilli()
}

fun Long.timestampMillisToLocateDateTime(zoneId: ZoneId): LocalDateTime {
    val timestamp = this.takeIf { it >= 0 } ?: return LocalDateTime.MIN
    return Instant.ofEpochMilli(timestamp).atZone(zoneId).toLocalDateTime()
}

fun String?.toLocalDateTime(
    formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
): LocalDateTime? {
    return try {
        this?.takeIf { it.isNotBlank() }?.let { time ->
            LocalDateTime.parse(time, formatter)
        }
    } catch (exception: DateTimeParseException) {
        null
    }
}

fun String?.toLocalDate(
    formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
): LocalDate? {
    return try {
        this?.takeIf { it.isNotBlank() }?.let { time ->
            LocalDate.parse(time, formatter)
        }
    } catch (exception: DateTimeParseException) {
        null
    }
}

fun String?.toZoneId(): ZoneId? {
    val zoneId = this?.takeIf { it.isNotBlank() } ?: return null
    return try {
        ZoneId.of(zoneId)
    } catch (exception: Throwable) {
        null
    }
}
