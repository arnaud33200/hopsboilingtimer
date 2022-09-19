package ca.arnaud.hopsboilingtimer.app.alarm

import android.app.AlarmManager
import android.content.Context
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.mapper.AdditionNotificationMapper
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeNextAdditionAlert
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdditionAlarmScheduler @Inject constructor(
    coroutineScopeProvider: CoroutineScopeProvider,
    private val alarmManager: AlarmManager,
    private val context: Context,
    private val additionNotificationMapper: AdditionNotificationMapper,
    private val subscribeNextAdditionAlert: SubscribeNextAdditionAlert,
) {

    init {
        coroutineScopeProvider.scope.launch {
            subscribeNextAdditionAlert.execute().collect { alert ->
                if (alert != null) {
                    val notification = additionNotificationMapper.mapTo(alert)
                    schedule(notification)
                }
            }
        }
    }

    private fun schedule(notification: AdditionNotification) {
        val pendingIntent = AdditionAlarmReceiver.createPendingIntent(context, notification)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            notification.triggerAtMillis,
            pendingIntent
        )
    }
}