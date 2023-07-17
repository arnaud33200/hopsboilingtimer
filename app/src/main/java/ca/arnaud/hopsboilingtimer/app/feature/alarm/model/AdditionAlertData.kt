package ca.arnaud.hopsboilingtimer.app.feature.alarm.model

import java.io.Serializable
import java.time.Duration

data class AdditionAlertData(
    val id: String,
    val initialDelay: Duration,
    val additions: List<AdditionData>,
    val type: AdditionAlertDataType,
    val duration: Duration,
) : Serializable

enum class AdditionAlertDataType : Serializable {
    START, NEXT, END
}

data class AdditionData(
    val name: String,
) : Serializable