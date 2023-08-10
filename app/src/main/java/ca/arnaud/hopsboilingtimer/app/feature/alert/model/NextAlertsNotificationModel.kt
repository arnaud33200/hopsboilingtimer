package ca.arnaud.hopsboilingtimer.app.feature.alert.model

import android.os.Parcelable
import androidx.annotation.RawRes
import kotlinx.parcelize.Parcelize

data class NextAlertsNotificationModel(
    val rows: List<NextAlertNotificationRowModel> = emptyList(),
    val dismissible: Boolean = true,
    @RawRes
    val soundRes: Int? = null, // TODO - would be better to have Uri but doesn't work with mediaplayer
)

@Parcelize
data class NextAlertNotificationRowModel(
    val id: String = "", // used for unit test
    val type: String = "",
    val title: String = "",
    val time: String = "",
) : Parcelable