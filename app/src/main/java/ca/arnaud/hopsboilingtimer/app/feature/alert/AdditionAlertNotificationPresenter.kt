package ca.arnaud.hopsboilingtimer.app.feature.alert

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import ca.arnaud.hopsboilingtimer.app.feature.alert.factory.AdditionAlertNotificationModelFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.factory.AlertAndroidNotificationFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.service.PermissionService
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import javax.inject.Inject

class AdditionAlertNotificationPresenter @Inject constructor(
    private val notificationManager: NotificationManagerCompat,
    private val alertAndroidNotificationFactory: AlertAndroidNotificationFactory,
    private val permissionService: PermissionService,
    private val additionAlertNotificationModelFactory: AdditionAlertNotificationModelFactory,
) {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val NOTIFICATION_ID = 12345
    }

    @SuppressLint("MissingPermission") // Already check in permission service
    fun show(
        additionAlert: AdditionAlertData,
        schedule: AdditionSchedule?,
        context: Context
    ) {
        if (schedule == null || !permissionService.hasNotificationPermission()) {
            return
        }

        val model = additionAlertNotificationModelFactory.create(additionAlert, schedule)
        val notification = alertAndroidNotificationFactory.createNotification(
            model, CHANNEL_ID, context
        )
        createChannelIfNeeded()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun cancel() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun createChannelIfNeeded() {
        val channel = notificationManager.notificationChannels.find { channel ->
            channel.id == CHANNEL_ID
        }
        if (channel != null) {
            return
        }

        notificationManager.createNotificationChannel(
            alertAndroidNotificationFactory.createChannel(CHANNEL_ID)
        )
    }
}