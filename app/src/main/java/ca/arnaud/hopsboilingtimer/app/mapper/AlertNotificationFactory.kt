package ca.arnaud.hopsboilingtimer.app.mapper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.alarm.AdditionNotification
import javax.inject.Inject

class AlertNotificationFactory @Inject constructor() {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
    }

    fun create(additionNotification: AdditionNotification, context: Context): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle("Hops Boiling Timer")
            setContentText(additionNotification.message)
            priority = NotificationCompat.PRIORITY_MAX
        }.build()
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