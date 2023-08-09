package ca.arnaud.hopsboilingtimer.app.feature.alert.model

import android.os.Parcelable
import androidx.annotation.RawRes
import kotlinx.parcelize.Parcelize

data class AdditionAlertNotificationModel(
    val rows: List<AlertNotificationRowModel> = emptyList(),
    val dismissible: Boolean = true,
    @RawRes
    val soundRes: Int? = null, // TODO - would be better to have Uri but doesn't work with mediaplayer
)

@Parcelize
data class AlertNotificationRowModel(
    val id: String = "", // used for unit test
    val type: String = "",
    val title: String = "",
    val time: String = "",
) : Parcelable