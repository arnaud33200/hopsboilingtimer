package ca.arnaud.hopsboilingtimer.domain.model

import java.time.Duration

data class AdditionAlert(
    val triggerAtTime: Long,
    val additions: List<Addition>
)

