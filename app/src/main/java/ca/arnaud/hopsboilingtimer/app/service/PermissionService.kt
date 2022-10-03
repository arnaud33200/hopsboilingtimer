package ca.arnaud.hopsboilingtimer.app.service

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import javax.inject.Inject

class PermissionService @Inject constructor(
    private val context: Context,
) {

    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        const val NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS
    }

    fun hasNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true // TODO - probably check disabled from settings
        }

        return when (
            ContextCompat.checkSelfPermission(context, NOTIFICATION_PERMISSION)
        ) {
            PERMISSION_GRANTED -> true
            else -> false
        }
    }

    fun shouldShowNotificationRequest(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || hasNotificationPermission()) {
            return false
        }

        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity, NOTIFICATION_PERMISSION
        )
    }
}