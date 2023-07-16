package ca.arnaud.hopsboilingtimer.app.feature.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ca.arnaud.hopsboilingtimer.app.di.common.HiltBroadcastReceiver
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.feature.alarm.model.AdditionNotificationModel
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.OnAdditionAlertReceived
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AdditionAlarmReceiver : HiltBroadcastReceiver() {

    companion object {
        private const val NOTIFICATION_EXTRA = "NOTIFICATION_EXTRA"
        private const val REQUEST_CODE = 666

        fun createPendingIntent(
            context: Context,
            notification: AdditionNotificationModel,
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
    lateinit var additionNotificationPresenter: AdditionNotificationPresenter

    @Inject
    lateinit var onAdditionAlertReceived: OnAdditionAlertReceived

    @Inject
    lateinit var coroutineScopeProvider: CoroutineScopeProvider

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        val alert = intent.getParcelableExtra<AdditionNotificationModel>(NOTIFICATION_EXTRA)
            ?: return

        additionNotificationPresenter.show(alert, context)

        coroutineScopeProvider.scope.launch {
            onAdditionAlertReceived.execute(OnAdditionAlertReceived.Params(alert.alertId))
        }
    }
}
