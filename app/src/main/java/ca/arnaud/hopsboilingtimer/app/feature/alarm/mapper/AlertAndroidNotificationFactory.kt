package ca.arnaud.hopsboilingtimer.app.feature.alarm.mapper

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
import ca.arnaud.hopsboilingtimer.app.feature.alarm.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alarm.model.AdditionAlertDataType
import ca.arnaud.hopsboilingtimer.app.feature.alarm.model.AdditionNotificationModel
import ca.arnaud.hopsboilingtimer.app.feature.common.mapper.DurationTextMapper
import javax.inject.Inject

class AlertAndroidNotificationFactory @Inject constructor(
    private val durationTextMapper: DurationTextMapper,
) {

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val CONTENT_INTENT_REQUEST_CODE = 3750
    }

    fun create(alert: AdditionAlertData, context: Context): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_notification_badge)
            setContentTitle("Hops Boiling Timer") // TODO - Hardcoded string
            setContentText(when (alert.type) {
                AdditionAlertDataType.START -> createStartMessage(alert)
                AdditionAlertDataType.NEXT -> createNextMessage(alert)
                AdditionAlertDataType.END -> createEndMessage(alert)
            })
            priority = NotificationCompat.PRIORITY_MAX
            setContentIntent(getContentIntent(context))
        }.build()
    }

    private fun createStartMessage(input: AdditionAlertData): String {
        val additions = input.additions
        val hops = additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return "Start$hops"
    }

    private fun createNextMessage(input: AdditionAlertData): String {
        val additions = input.additions
        val duration = input.duration
        val hops = additions.joinToString(separator = ", ", prefix = ": ") { it.name }
        return "Next Additions (${durationTextMapper.mapTo(duration)})$hops"
    }

    private fun createEndMessage(input: AdditionAlertData): String {
        return "Stop Boiling!"
    }

    fun create(additionNotificationModel: AdditionNotificationModel, context: Context): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_notification_badge)
            setContentTitle("Hops Boiling Timer") // TODO - Hardcoded string
            setContentText(additionNotificationModel.message)
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
        val name = "Hops Additions" // TODO - Hardcoded string
        val descriptionText = "Add the next additions" // TODO - Hardcoded string
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