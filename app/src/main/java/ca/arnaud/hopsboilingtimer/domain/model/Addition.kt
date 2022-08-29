package ca.arnaud.hopsboilingtimer.domain.model

import java.time.Duration

data class Addition(
    val name: String,
    val duration: Duration
)