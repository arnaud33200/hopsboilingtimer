package ca.arnaud.hopsboilingtimer.app.mapper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.MainActivity
import ca.arnaud.hopsboilingtimer.app.alarm.AdditionNotification
import javax.inject.Inject

class AlertNotificationFactory @Inject constructor() {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val CONTENT_INTENT_REQUEST_CODE = 3750
    }

    fun create(additionNotification: AdditionNotification, context: Context): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle("Hops Boiling Timer")
            setContentText(additionNotification.message)
            priority = NotificationCompat.PRIORITY_MAX
            setContentIntent(getContentIntent(context))
        }.build()
    }

    private fun getContentIntent(context: Context): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        return PendingIntent.getActivity(context, CONTENT_INTENT_REQUEST_CODE, intent, flags)
    }

    fun createChannel(context: Context) {
        val name = "Hops Additions"
        val descriptionText = "Add the next additions"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager = ContextCompat.getSystemService(
            context, NotificationManager::class.java
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}