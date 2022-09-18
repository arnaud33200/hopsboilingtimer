package ca.arnaud.hopsboilingtimer.domain.fake

import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider

class FakeTimeProvider: TimeProvider {

    var nowMillis = 0L

    override fun getNowTimeMillis(): Long {
        return nowMillis
    }
}