package ca.arnaud.hopsboilingtimer.app.feature.alert

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import ca.arnaud.hopsboilingtimer.app.feature.alert.factory.AlertAndroidNotificationFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import java.util.Random
import javax.inject.Inject

class AdditionAlertNotificationPresenter @Inject constructor(
    private val notificationManager: NotificationManager,
    private val alertAndroidNotificationFactory: AlertAndroidNotificationFactory,
) {
    // TODO - show one static notification and update on every new

    fun show(additionAlert: AdditionAlertData, context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }

        val permission = ActivityCompat.checkSelfPermission(context, POST_NOTIFICATIONS)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            return
        }

        // TODO - do it on init?
        createChannel()
        val notification = alertAndroidNotificationFactory.createNotification(additionAlert, context)
        show(notification, context)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(POST_NOTIFICATIONS)
    private fun show(notification: Notification, context: Context) {
        // TODO - use id from the alert, probably make the id int
        val notificationId = Random().nextInt(1000 - 1) + 1
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    private fun createChannel() {
        val channel = alertAndroidNotificationFactory.createChannel()
        notificationManager.createNotificationChannel(channel)
    }
}