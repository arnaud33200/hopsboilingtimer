package ca.arnaud.hopsboilingtimer.domain.model

import java.time.Duration

data class AdditionAlert(
    val countDown: Duration,
    val additions: List<Addition>
)

