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
import ca.arnaud.hopsboilingtimer.app.feature.alarm.model.AdditionNotificationModel
import ca.arnaud.hopsboilingtimer.app.service.PermissionService
import java.util.Random
import javax.inject.Inject

class AdditionNotificationPresenter @Inject constructor(
    private val permissionService: PermissionService,
    private val alertAndroidNotificationFactory: AlertAndroidNotificationFactory,
) {
    // TODO - show one static notification and update on every new

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