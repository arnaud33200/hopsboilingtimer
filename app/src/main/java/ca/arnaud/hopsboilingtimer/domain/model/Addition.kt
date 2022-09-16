package ca.arnaud.hopsboilingtimer.domain.model

import java.time.Duration
import java.util.*

data class Addition(
    val id: String =  UUID.randomUUID().toString(),
    val name: String,
    val duration: Duration
)