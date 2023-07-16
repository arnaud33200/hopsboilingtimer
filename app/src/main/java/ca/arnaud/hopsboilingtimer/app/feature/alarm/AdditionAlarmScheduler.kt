package ca.arnaud.hopsboilingtimer.app.feature.alarm

import android.app.AlarmManager
import android.content.Context
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.feature.alarm.mapper.AdditionNotificationModelMapper
import ca.arnaud.hopsboilingtimer.app.feature.alarm.model.AdditionNotificationModel
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeNextAdditionAlert
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdditionAlarmScheduler @Inject constructor(
    coroutineScopeProvider: CoroutineScopeProvider,
    private val alarmManager: AlarmManager,
    private val context: Context,
    private val additionNotificationModelMapper: AdditionNotificationModelMapper,
    private val subscribeNextAdditionAlert: SubscribeNextAdditionAlert,
    private val subscribeAdditionSchedule: SubscribeAdditionSchedule,
) {

    init {
        coroutineScopeProvider.scope.launch {
            subscribeNextAdditionAlert.execute().collect { alert ->
                if (alert != null) {
                    val notification = additionNotificationModelMapper.mapTo(alert)
                    schedule(notification)
                }
            }
        }
        coroutineScopeProvider.scope.launch {
            subscribeAdditionSchedule.execute().collect { schedule ->
                if (schedule == null) {
                    cancelAlarm()
                }
            }
        }
    }

    private fun cancelAlarm() {
        val notification = AdditionNotificationModel("", "", 0L)
        val pendingIntent = AdditionAlarmReceiver.createPendingIntent(context, notification)
        alarmManager.cancel(pendingIntent)
    }

    private fun schedule(notification: AdditionNotificationModel) {
        val pendingIntent = AdditionAlarmReceiver.createPendingIntent(context, notification)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            notification.triggerAtMillis,
            pendingIntent
        )
    }
}