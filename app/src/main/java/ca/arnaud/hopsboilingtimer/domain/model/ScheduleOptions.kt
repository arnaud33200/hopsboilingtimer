package ca.arnaud.hopsboilingtimer.domain.model

import java.time.Duration

data class ScheduleOptions(
    val delay: Duration? = null
)