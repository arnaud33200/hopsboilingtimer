package ca.arnaud.hopsboilingtimer.app.feature.alert.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class AdditionAlertNotificationModel(
    val rows: List<AlertNotificationRowModel> = emptyList(),
)

@Parcelize
data class AlertNotificationRowModel(
    val title: String,
    val time: String,
) : Parcelable