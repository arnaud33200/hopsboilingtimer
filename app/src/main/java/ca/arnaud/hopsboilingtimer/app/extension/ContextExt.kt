package ca.arnaud.hopsboilingtimer.app.extension

import android.content.Context
import android.content.Intent
import android.provider.Settings

fun Context.launchNotificationPermissionSettings() {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}