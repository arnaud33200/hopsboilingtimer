package ca.arnaud.hopsboilingtimer.app.provider

import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import javax.inject.Inject

class TimeProviderImpl @Inject constructor(): TimeProvider {

    override fun getNowTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}