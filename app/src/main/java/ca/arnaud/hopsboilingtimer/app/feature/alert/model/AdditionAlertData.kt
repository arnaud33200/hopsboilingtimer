package ca.arnaud.hopsboilingtimer.app.feature.alert.model

import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import java.io.Serializable
import java.time.Duration
import java.time.LocalDateTime

data class AdditionAlertData(
    val initialDelay: Duration,
    val comingAlert: BaseAdditionAlertData,
    val nextAlerts: List<BaseAdditionAlertData>, // avoiding infinite loop
) : Serializable

data class BaseAdditionAlertData(
    val id: String,
    val additions: List<AdditionData>,
    val type: AdditionAlertDataType,
    val duration: Duration,
    val scheduleAt: LocalDateTime
)  : Serializable

enum class AdditionAlertDataType : Serializable {
    START, NEXT, END
}

data class AdditionData(
    val name: String,
) : Serializable