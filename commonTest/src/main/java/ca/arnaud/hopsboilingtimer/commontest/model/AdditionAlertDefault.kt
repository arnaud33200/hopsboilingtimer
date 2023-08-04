package ca.arnaud.hopsboilingtimer.commontest.model

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import java.time.Duration
import java.time.LocalDateTime

val additionAlertStartDefault = AdditionAlert.Start(
    id = "",
    triggerAtTime = LocalDateTime.MIN,
    additions = emptyList(),
    checked = false,
)

val additionAlertNextDefault = AdditionAlert.Next(
    id = "",
    triggerAtTime = LocalDateTime.MIN,
    additions = emptyList(),
    checked = false,
)

val additionAlertEndDefault = AdditionAlert.End(
    id = "",
    triggerAtTime = LocalDateTime.MIN,
    duration = Duration.ZERO,
)