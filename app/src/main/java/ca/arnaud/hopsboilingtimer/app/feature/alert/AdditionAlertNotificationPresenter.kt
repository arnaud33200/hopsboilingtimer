package ca.arnaud.hopsboilingtimer.app.feature.alert

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.core.app.NotificationManagerCompat
import ca.arnaud.hopsboilingtimer.app.feature.alert.factory.AlertAndroidNotificationFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.factory.NextAlertsNotificationModelFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.factory.NowAlertNotificationModelFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertDataType
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.NextAlertsNotificationModel
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.NowAlertNotificationModel
import ca.arnaud.hopsboilingtimer.app.service.PermissionService
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import javax.inject.Inject

class AdditionAlertNotificationPresenter @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat,
    private val alertAndroidNotificationFactory: AlertAndroidNotificationFactory,
    private val permissionService: PermissionService,
    private val nextAlertsNotificationModelFactory: NextAlertsNotificationModelFactory,
    private val nowAlertNotificationModelFactory: NowAlertNotificationModelFactory,
) {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val NEXT_ALERTS_NOTIFICATION_ID = 12345
        private const val NOW_ALERT_NOTIFICATION_ID = 23456
    }

    fun show(
        alertData: AdditionAlertData,
        schedule: AdditionSchedule?,
        context: Context = this.context,
    ) {
        when (alertData.type) {
            AdditionAlertDataType.Alert -> {
                showNextAlerts(alertData, schedule, context)
                showNowAlert(alertData, schedule, context)
            }

            AdditionAlertDataType.Reminder -> {
                showNowAlert(alertData, schedule, context)
            }
        }
    }

    private fun showNextAlerts(
        alertData: AdditionAlertData,
        schedule: AdditionSchedule?,
        context: Context = this.context,
    ) {
        showNextAlerts(
            model = nextAlertsNotificationModelFactory.create(alertData, schedule),
            context = context,
        )
    }

    private fun showNextAlerts(
        model: NextAlertsNotificationModel?,
        context: Context = this.context,
    ) {
        if (model == null) {
            notificationManager.cancel(NEXT_ALERTS_NOTIFICATION_ID)
            return
        }

        val notification = alertAndroidNotificationFactory.createNextAlerts(
            model, CHANNEL_ID, context
        )
        createChannelIfNeeded()
        model.soundRes?.let { res -> playNotificationSound(res) }
        showNotification(notification, NEXT_ALERTS_NOTIFICATION_ID)
    }

    private fun showNowAlert(
        alertData: AdditionAlertData,
        schedule: AdditionSchedule?,
        context: Context
    ) {
        showNowAlert(
            model = nowAlertNotificationModelFactory.createAddHops(alertData, schedule),
            context = context,
        )
    }

    fun showEndAlert() {
        showNowAlert(
            model = nowAlertNotificationModelFactory.createEnd()
        )
    }

    private fun showNowAlert(
        model: NowAlertNotificationModel?,
        context: Context = this.context,
    ) {
        if (model == null) {
            notificationManager.cancel(NOW_ALERT_NOTIFICATION_ID)
            return
        }

        val notification = alertAndroidNotificationFactory.createNowAlert(
            model, CHANNEL_ID, context
        )
        if (showNotification(notification, NOW_ALERT_NOTIFICATION_ID)) {
            model.soundRes?.let { res -> playNotificationSound(res) }
        }
    }

    // TODO - rename hide all notifications
    fun hideNotifications() {
        notificationManager.cancel(NOW_ALERT_NOTIFICATION_ID)
        notificationManager.cancel(NEXT_ALERTS_NOTIFICATION_ID)
    }

    private fun playNotificationSound(@RawRes id: Int) {
        MediaPlayer.create(context, id)?.let { mediaPlayer ->
            mediaPlayer.setOnCompletionListener { player -> player.release() }
            mediaPlayer.start()
        }
    }

    @SuppressLint("MissingPermission") // Already check in permission service
    private fun showNotification(
        notification: Notification,
        notificationId: Int,
    ): Boolean {
        if (!permissionService.hasNotificationPermission()) {
            return false
        }

        createChannelIfNeeded()
        notificationManager.notify(notificationId, notification)
        return true
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