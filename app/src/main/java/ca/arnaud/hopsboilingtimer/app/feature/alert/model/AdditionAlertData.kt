package ca.arnaud.hopsboilingtimer.app.feature.alert.model

import java.io.Serializable
import java.time.Duration

data class AdditionAlertData(
    val id: String,
    val initialDelay: Duration,
) : Serializable