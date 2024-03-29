package ca.arnaud.hopsboilingtimer.app.service

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_ALARMS
import android.app.NotificationManager.INTERRUPTION_FILTER_ALL
import android.app.NotificationManager.INTERRUPTION_FILTER_NONE
import android.app.NotificationManager.INTERRUPTION_FILTER_PRIORITY
import android.app.NotificationManager.INTERRUPTION_FILTER_UNKNOWN
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import javax.inject.Inject

class PermissionService @Inject constructor(
    private val context: Context,
    private val notificationManager: NotificationManager,
) {

    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        const val NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS
    }

    fun hasNotificationPermission(): Boolean {
        val enabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            enabled && context.isPermissionGranted(listOf(NOTIFICATION_PERMISSION))
        } else {
            enabled
        }
    }

    fun Context.isPermissionGranted(permissions: List<String>): Boolean {
        return permissions.all { isPermissionGranted(it) }
    }

    fun Context.isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED
    }

    fun shouldShowNotificationRequest(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || hasNotificationPermission()) {
            return false
        }

        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity, NOTIFICATION_PERMISSION
        )
    }

    fun willNotificationBeVisible(): Boolean {
        return when (notificationManager.currentInterruptionFilter) {
            INTERRUPTION_FILTER_ALL -> true
            INTERRUPTION_FILTER_ALARMS,
            INTERRUPTION_FILTER_NONE,
            INTERRUPTION_FILTER_PRIORITY,
            INTERRUPTION_FILTER_UNKNOWN -> false

            else -> false
        }
    }
}