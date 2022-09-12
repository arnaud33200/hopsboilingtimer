package ca.arnaud.hopsboilingtimer.domain.provider

interface TimeProvider {

    fun getNowTimeMillis(): Long
}