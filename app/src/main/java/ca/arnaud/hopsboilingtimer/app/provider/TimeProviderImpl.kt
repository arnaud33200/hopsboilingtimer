package ca.arnaud.hopsboilingtimer.app.provider

import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class TimeProviderImpl @Inject constructor(): TimeProvider {

    override fun getNowLocalDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

    override fun getCurrentZoneId(): ZoneId {
        return ZoneId.systemDefault()
    }
}