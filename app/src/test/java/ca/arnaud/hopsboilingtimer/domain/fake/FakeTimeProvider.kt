package ca.arnaud.hopsboilingtimer.domain.fake

import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.LocalDateTime
import java.time.ZoneId

class FakeTimeProvider: TimeProvider {

    var now = LocalDateTime.MIN
    val zoneId = ZoneId.of("UTC")

    override fun getNowLocalDateTime(): LocalDateTime {
        return now
    }

    override fun getCurrentZoneId(): ZoneId {
        return zoneId
    }
}