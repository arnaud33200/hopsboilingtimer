package ca.arnaud.hopsboilingtimer.app.feature.alert

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import androidx.core.app.NotificationManagerCompat
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
        private const val NOTIFICATION_ID = 12345
    }

    fun show(
        additionAlert: AdditionAlertData,
        schedule: AdditionSchedule?,
        context: Context,
    ) {
        if (schedule == null) {
            return
        }

        val model = nextAlertsNotificationModelFactory.create(additionAlert, schedule)
        showNotification(model, context)
    }

    fun showEnd() {
        val model = nextAlertsNotificationModelFactory.createEnd()
        showNotification(model, context)
    }

    fun cancel() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    @SuppressLint("MissingPermission") // Already check in permission service
    private fun showNotification(
        model: NextAlertsNotificationModel,
        context: Context,
    ) {
        if (!permissionService.hasNotificationPermission()) {
            return
        }

        val notification = alertAndroidNotificationFactory.createNextAlerts(
            model, CHANNEL_ID, context
        )
        createChannelIfNeeded()
        notificationManager.notify(NOTIFICATION_ID, notification)

        model.soundRes?.let { res ->
            MediaPlayer.create(context, res)?.let { mediaPlayer ->
                mediaPlayer.setOnCompletionListener { player -> player.release() }
                mediaPlayer.start()
            }
        }
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