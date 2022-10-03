package ca.arnaud.hopsboilingtimer.app.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.mapper.AlertNotificationFactory
import ca.arnaud.hopsboilingtimer.app.service.PermissionService
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.OnAdditionAlertReceived
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AdditionAlarmReceiver : HiltBroadcastReceiver() {

    companion object {
        private const val NOTIFICATION_EXTRA = "NOTIFICATION_EXTRA"
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

    @Inject
    lateinit var alertNotificationFactory: AlertNotificationFactory

    @Inject
    lateinit var permissionService: PermissionService

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val alert = intent.getParcelableExtra<AdditionNotification>(NOTIFICATION_EXTRA) ?: return

        showNotification(alert, context)

        coroutineScopeProvider.scope.launch {
            onAdditionAlertReceived.execute(OnAdditionAlertReceived.Params(alert.alertId))
        }
    }

    private fun showNotification(alert: AdditionNotification, context: Context) {
        if (!permissionService.hasNotificationPermission()) {
            return
        }

        alertNotificationFactory.createChannel(context)
        val notification = alertNotificationFactory.create(alert, context)

        // TODO - use id from the alert, probably make the id int
        val notificationId = Random().nextInt(1000 - 1) + 1
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
}
