package ca.arnaud.hopsboilingtimer.app.model

import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertDataType
import java.time.Duration

val additionAlertDataDefault = AdditionAlertData(
    id = "",
    initialDelay = Duration.ZERO,
    type = AdditionAlertDataType.Alert,
)