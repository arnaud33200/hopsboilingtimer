package ca.arnaud.hopsboilingtimer.domain.mapper

import java.util.UUID
import javax.inject.Inject

class IdFactory @Inject constructor() {

    fun createRandomId(): String {
        return UUID.randomUUID().toString()
    }
}