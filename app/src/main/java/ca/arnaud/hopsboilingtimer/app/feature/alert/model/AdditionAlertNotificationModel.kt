package ca.arnaud.hopsboilingtimer.app.feature.alert.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class AdditionAlertNotificationModel(
    val rows: List<AlertNotificationRowModel> = emptyList(),
    val dismissible: Boolean = true,
)

@Parcelize
data class AlertNotificationRowModel(
    val id: String = "", // used for unit test
    val type: String = "",
    val title: String = "",
    val time: String = "",
) : Parcelable