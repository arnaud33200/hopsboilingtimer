package ca.arnaud.hopsboilingtimer.app.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import ca.arnaud.hopsboilingtimer.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt


@AndroidEntryPoint
class AdditionAlarmReceiver : HiltBroadcastReceiver() {

    // TODO - split into different manager, the receiver should just received and call the use case
    //  other class would then listen the right
    //  probably wake up other class if the application is not started

    companion object {
        private const val SCHEDULE_EXTRA = "SCHEDULE_EXTRA"
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val REQUEST_CODE = 666

        fun createPendingIntent(
            context: Context,
            additionNotificationSchedule: AdditionNotificationSchedule
        ): PendingIntent? {
            val intent = Intent(context, AdditionAlarmReceiver::class.java).apply {
                putExtra(SCHEDULE_EXTRA, additionNotificationSchedule)
                putExtra("whatever", "want to see it")
            }
            return PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
    }

    @Inject
    lateinit var additionAlarmScheduler: AdditionAlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val schedule = intent.getParcelableExtra<AdditionNotificationSchedule>(SCHEDULE_EXTRA)
        val alert = schedule?.AdditionNotifications?.firstOrNull() ?: return

        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mp: MediaPlayer = MediaPlayer.create(context, uri)
        mp.start()

        // TODO - put in factory
        val notification = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
//            setContentTitle(textTitle)
            setContentText(alert.message)
            priority = NotificationCompat.PRIORITY_MAX
        }.build()

        createNotificationChannel(context)
        val notificationId = Random().nextInt(1000 - 1) + 1
        NotificationManagerCompat.from(context).notify(notificationId, notification)

        // TODO - call the use case SetAdditioonAlertReceived
        additionAlarmScheduler.scheduleNext(schedule)
    }

    private fun createNotificationChannel(context: Context) {
        val name = "Hops Additions"
        val descriptionText = "Add the next additions"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager = getSystemService(
            context, NotificationManager::class.java
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
