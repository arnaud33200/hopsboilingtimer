package ca.arnaud.hopsboilingtimer.app.model

import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertDataType
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.BaseAdditionAlertData
import java.time.Duration
import java.time.LocalDateTime

val baseAdditionAlertDataDefault = BaseAdditionAlertData(
    id = "",
    additions = emptyList(),
    type = AdditionAlertDataType.START,
    duration = Duration.ZERO,
    scheduleAt = LocalDateTime.MIN,
)

val additionAlertDataDefault = AdditionAlertData(
    initialDelay = Duration.ZERO,
    comingAlert = baseAdditionAlertDataDefault,
    nextAlerts = emptyList(),
)