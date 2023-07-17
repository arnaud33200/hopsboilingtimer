package ca.arnaud.hopsboilingtimer.app.feature.alarm

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import ca.arnaud.hopsboilingtimer.app.feature.alarm.mapper.AlertAndroidNotificationFactory
import ca.arnaud.hopsboilingtimer.app.feature.alarm.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alarm.model.AdditionNotificationModel
import java.util.Random
import javax.inject.Inject

class AdditionNotificationPresenter @Inject constructor(
    private val alertAndroidNotificationFactory: AlertAndroidNotificationFactory,
) {
    // TODO - show one static notification and update on every new

    // TODO - move create channel here

    fun show(additionAlert: AdditionAlertData, context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }

        val permission = ActivityCompat.checkSelfPermission(context, POST_NOTIFICATIONS)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            return
        }

        alertAndroidNotificationFactory.createChannel(context)
        val notification = alertAndroidNotificationFactory.create(additionAlert, context)
        show(notification, context)
    }

    @Deprecated(message = "Use the show with AdditionAlert")
    fun show(model: AdditionNotificationModel, context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }

        val permission = ActivityCompat.checkSelfPermission(context, POST_NOTIFICATIONS)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            return
        }

        alertAndroidNotificationFactory.createChannel(context)
        val notification = alertAndroidNotificationFactory.create(model, context)
        show(notification, context)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(POST_NOTIFICATIONS)
    private fun show(notification: Notification, context: Context) {
        // TODO - use id from the alert, probably make the id int
        val notificationId = Random().nextInt(1000 - 1) + 1
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
}