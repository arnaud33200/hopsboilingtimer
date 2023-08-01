package ca.arnaud.hopsboilingtimer.domain.model

import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import java.time.LocalDateTime

val additionScheduleDefault = AdditionSchedule(
    startingTime = LocalDateTime.MIN,
    alerts = emptyList()
)