package ca.arnaud.hopsboilingtimer.app.feature.alert

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import ca.arnaud.hopsboilingtimer.app.feature.alert.factory.AlertAndroidNotificationFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.service.PermissionService
import javax.inject.Inject

class AdditionAlertNotificationPresenter @Inject constructor(
    private val notificationManager: NotificationManager,
    private val alertAndroidNotificationFactory: AlertAndroidNotificationFactory,
    private val permissionService: PermissionService,
) {
    // TODO - setup custom layout

    companion object {
        private const val NOTIFICATION_ID = 12345
    }

    init {
        val channel = alertAndroidNotificationFactory.createChannel()
        notificationManager.createNotificationChannel(channel)
    }

    fun show(additionAlert: AdditionAlertData, context: Context) {
        if (!permissionService.hasNotificationPermission()) {
            return
        }

        val notification = alertAndroidNotificationFactory.createNotification(additionAlert, context)
        show(notification, context)
    }

    @SuppressLint("MissingPermission", "NewApi")
    private fun show(notification: Notification, context: Context) {
        val notificationId = NOTIFICATION_ID // force updating the same notification
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
}