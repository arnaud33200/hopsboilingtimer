package ca.arnaud.hopsboilingtimer.local.mapper

import ca.arnaud.hopsboilingtimer.domain.common.TwoWayMapper
import ca.arnaud.hopsboilingtimer.domain.extension.timestampMillisToLocateDateTime
import ca.arnaud.hopsboilingtimer.domain.extension.toEpochMillis
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.LocalDateTime
import javax.inject.Inject

class EntityTimeMapper @Inject constructor(
    private val timeProvider: TimeProvider
) : TwoWayMapper<LocalDateTime?, Long> {

    companion object {
        const val NULL_TIME_VALUE = -1L
    }

    override fun mapTo(input: LocalDateTime?): Long {
        return try {
            input?.toEpochMillis(timeProvider.getCurrentZoneId())
        } catch (exception: Throwable) {
            null
        } ?: NULL_TIME_VALUE
    }

    override fun mapFrom(output: Long): LocalDateTime? {
        return output.takeIf { it > NULL_TIME_VALUE }?.timestampMillisToLocateDateTime(
            zoneId = timeProvider.getCurrentZoneId()
        )
    }
}