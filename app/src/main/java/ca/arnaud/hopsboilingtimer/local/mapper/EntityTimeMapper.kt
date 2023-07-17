package ca.arnaud.hopsboilingtimer.local.mapper

import ca.arnaud.hopsboilingtimer.domain.common.TwoWayMapper
import ca.arnaud.hopsboilingtimer.domain.extension.timestampMillisToLocateDateTime
import ca.arnaud.hopsboilingtimer.domain.extension.toEpochMillis
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.LocalDateTime
import javax.inject.Inject

class EntityTimeMapper @Inject constructor(
    private val timeProvider: TimeProvider
) : TwoWayMapper<LocalDateTime, Long> {

    override fun mapTo(input: LocalDateTime): Long {
        return input.toEpochMillis()
    }

    override fun mapFrom(output: Long): LocalDateTime {
        return output.timestampMillisToLocateDateTime(
            zoneId = timeProvider.getCurrentZoneId()
        )
    }
}