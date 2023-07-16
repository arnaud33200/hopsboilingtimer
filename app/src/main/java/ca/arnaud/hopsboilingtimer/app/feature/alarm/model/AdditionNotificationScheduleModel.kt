package ca.arnaud.hopsboilingtimer.app.feature.alarm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdditionNotificationScheduleModel(
    val additionNotificationModels: List<AdditionNotificationModel>
) : Parcelable

@Parcelize
data class AdditionNotificationModel(
    val alertId: String,
    val message: String,
    val triggerAtMillis: Long
) : Parcelable