package ca.arnaud.hopsboilingtimer.app.feature.alert.model

import androidx.annotation.RawRes

data class NowAlertNotificationModel(
    val title: String = "",
    val text: String? = null,
    @RawRes
    val soundRes: Int? = null,
)