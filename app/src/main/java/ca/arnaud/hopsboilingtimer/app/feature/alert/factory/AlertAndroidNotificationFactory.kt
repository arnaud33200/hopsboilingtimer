package ca.arnaud.hopsboilingtimer.app.feature.alert.factory

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.MainActivity
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertNotificationModel
import ca.arnaud.hopsboilingtimer.app.feature.alert.view.AdditionAlertNotificationView
import javax.inject.Inject

class AlertAndroidNotificationFactory @Inject constructor() {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val CONTENT_INTENT_REQUEST_CODE = 3750
    }

    fun createChannel(): NotificationChannel {
        val name = "Hops Additions" // TODO - Hardcoded string
        val descriptionText = "Add the next additions" // TODO - Hardcoded string
        val importance = NotificationManager.IMPORTANCE_HIGH

        return NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
    }

    fun createNotification(model: AdditionAlertNotificationModel, context: Context): Notification {
        val notificationLayout = AdditionAlertNotificationView(context.applicationContext).apply {
            bind(model = model, context = context)
        }
        return NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_notification_badge)
            setContentTitle("Hops Boiling Timer") // TODO - Hardcoded string
            setCustomContentView(notificationLayout)
            setContentIntent(getContentIntent(context))
            // setStyle(NotificationCompat.DecoratedCustomViewStyle()) // put back if need the classic notification style
            // setCustomBigContentView(notificationLayoutExpanded) // use if we need a separate bigger style
        }.build()
    }

    private fun getContentIntent(context: Context): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        return PendingIntent.getActivity(context, CONTENT_INTENT_REQUEST_CODE, intent, flags)
    }
}