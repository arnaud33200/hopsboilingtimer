package ca.arnaud.hopsboilingtimer.commontest.model

import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import java.time.LocalDateTime

val additionScheduleDefault = AdditionSchedule(
    startingTime = LocalDateTime.MIN,
    alerts = emptyList(),
    pauseTime = null,
)