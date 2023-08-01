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
import ca.arnaud.hopsboilingtimer.app.provider.StringProvider
import javax.inject.Inject

class AlertAndroidNotificationFactory @Inject constructor(
    private val stringProvider: StringProvider,
) {

    companion object {
        private const val CONTENT_INTENT_REQUEST_CODE = 3750
    }

    fun createChannel(channelId: String): NotificationChannel {
        val name = stringProvider.get(R.string.notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_HIGH

        return NotificationChannel(channelId, name, importance).apply {
            description = stringProvider.get(R.string.notification_channel_description)
        }
    }

    fun createNotification(model: AdditionAlertNotificationModel, channelId: String, context: Context): Notification {
        val notificationLayout = AdditionAlertNotificationView(context.applicationContext).apply {
            bind(model = model, context = context)
        }
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_notification_badge)
            setContentTitle(stringProvider.get(R.string.notification_title))
            setCustomContentView(notificationLayout)
            setContentIntent(getContentIntent(context))
            setOngoing(!model.dismissible)
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