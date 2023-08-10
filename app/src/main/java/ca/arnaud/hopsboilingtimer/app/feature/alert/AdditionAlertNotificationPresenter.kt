package ca.arnaud.hopsboilingtimer.app.feature.alert

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.media.MediaPlayer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.feature.alert.factory.AlertAndroidNotificationFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.factory.NextAlertsNotificationModelFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.NextAlertsNotificationModel
import ca.arnaud.hopsboilingtimer.app.service.PermissionService
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import javax.inject.Inject

class AdditionAlertNotificationPresenter @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManagerCompat,
    private val alertAndroidNotificationFactory: AlertAndroidNotificationFactory,
    private val permissionService: PermissionService,
    private val nextAlertsNotificationModelFactory: NextAlertsNotificationModelFactory,
) {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val NEXT_ALERTS_NOTIFICATION_ID = 12345
        private const val NOW_ALERT_NOTIFICATION_ID = 23456
    }

    fun showNextAlerts(
        alertData: AdditionAlertData,
        schedule: AdditionSchedule?,
        context: Context = this.context,
    ) {
        nextAlertsNotificationModelFactory.create(alertData, schedule)?.let { model ->
            showNextAlerts(model, context)
        }
    }

    fun showNowAlert(alertData: AdditionAlertData, schedule: AdditionSchedule?, context: Context) {
        // TODO - put in the factory
        val notification = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_notification_badge)
            setContentTitle("Now")
            setContentText("add the hops please")
//            setContentIntent(getContentIntent(context))
            setSound(null)

        }.build()
        showNotification(notification, NOW_ALERT_NOTIFICATION_ID)
    }

    fun showEndAlert() {
        // TODO - use the show now alert
        val model = nextAlertsNotificationModelFactory.createEnd()
        showNextAlerts(model)
    }

    private fun showNextAlerts(
        model: NextAlertsNotificationModel,
        context: Context = this.context,
    ) {
        val notification = alertAndroidNotificationFactory.createNextAlerts(
            model, CHANNEL_ID, context
        )
        createChannelIfNeeded()
        model.soundRes?.let { res ->
            MediaPlayer.create(context, res)?.let { mediaPlayer ->
                mediaPlayer.setOnCompletionListener { player -> player.release() }
                mediaPlayer.start()
            }
        }
        showNotification(notification, NEXT_ALERTS_NOTIFICATION_ID)
    }

    fun hideNotifications() {
        notificationManager.cancel(NEXT_ALERTS_NOTIFICATION_ID)
    }

    @SuppressLint("MissingPermission") // Already check in permission service
    private fun showNotification(
        notification: Notification,
        notificationId: Int,
    ) {
        if (!permissionService.hasNotificationPermission()) {
            return
        }

        notificationManager.notify(notificationId, notification)
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