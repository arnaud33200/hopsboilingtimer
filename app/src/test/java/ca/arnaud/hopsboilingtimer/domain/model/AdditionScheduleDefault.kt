package ca.arnaud.hopsboilingtimer.domain.model

import java.time.LocalDateTime

val additionScheduleDefault = AdditionSchedule(
    startingTime = LocalDateTime.MIN,
    alerts = emptyList()
)