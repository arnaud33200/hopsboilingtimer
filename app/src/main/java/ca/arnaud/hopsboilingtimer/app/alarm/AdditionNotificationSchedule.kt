package ca.arnaud.hopsboilingtimer.app.alarm

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdditionNotificationSchedule(
    val AdditionNotifications: List<AdditionNotification>
) : Parcelable

@Parcelize
data class AdditionNotification(
    val alertId: String,
    val message: String,
    val triggerAtMillis: Long
) : Parcelable