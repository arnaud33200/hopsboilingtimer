package ca.arnaud.hopsboilingtimer.app.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.OnAdditionAlertReceived
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AdditionAlarmReceiver : HiltBroadcastReceiver() {

    companion object {
        private const val NOTIFICATION_EXTRA = "NOTIFICATION_EXTRA"
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val REQUEST_CODE = 666

        fun createPendingIntent(
            context: Context,
            notification: AdditionNotification,
        ): PendingIntent? {
            val intent = Intent(context, AdditionAlarmReceiver::class.java).apply {
                putExtra(NOTIFICATION_EXTRA, notification)
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
    lateinit var onAdditionAlertReceived: OnAdditionAlertReceived

    @Inject
    lateinit var coroutineScopeProvider: CoroutineScopeProvider

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val alert = intent.getParcelableExtra<AdditionNotification>(NOTIFICATION_EXTRA) ?: return

//        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val mp: MediaPlayer = MediaPlayer.create(context, uri)
//        mp.start()

        // TODO - put in factory
        val notification = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
//            setContentTitle(textTitle)
            setContentText(alert.message)
            priority = NotificationCompat.PRIORITY_MAX
        }.build()

        createNotificationChannel(context)
        // TODO - use id from the alert, probably make the id int
        val notificationId = Random().nextInt(1000 - 1) + 1
        NotificationManagerCompat.from(context).notify(notificationId, notification)

        coroutineScopeProvider.scope.launch {
            onAdditionAlertReceived.execute(OnAdditionAlertReceived.Params(alert.alertId))
        }
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
