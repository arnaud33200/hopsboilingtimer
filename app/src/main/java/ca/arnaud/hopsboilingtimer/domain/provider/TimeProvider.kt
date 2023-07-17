package ca.arnaud.hopsboilingtimer.domain.provider

import java.time.LocalDateTime
import java.time.ZoneId


interface TimeProvider {

    fun getNowTimeMillis(): Long

    fun getNowLocalDateTime(): LocalDateTime

    fun getCurrentZoneId(): ZoneId
}