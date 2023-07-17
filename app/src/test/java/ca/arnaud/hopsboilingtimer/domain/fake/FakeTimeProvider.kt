package ca.arnaud.hopsboilingtimer.domain.fake

import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.LocalDateTime
import java.time.ZoneId

class FakeTimeProvider: TimeProvider {

    var nowMillis = 0L

    override fun getNowTimeMillis(): Long {
        return nowMillis
    }

    override fun getNowLocalDateTime(): LocalDateTime {
        TODO("Not yet implemented")
    }

    override fun getCurrentZoneId(): ZoneId {
        TODO("Not yet implemented")
    }
}