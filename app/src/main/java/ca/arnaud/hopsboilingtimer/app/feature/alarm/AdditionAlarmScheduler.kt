package ca.arnaud.hopsboilingtimer.app.feature.alarm

import android.app.AlarmManager
import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.feature.alarm.mapper.AdditionAlarmWorkerDataMapper
import ca.arnaud.hopsboilingtimer.app.feature.alarm.mapper.AdditionAlertDataMapper
import ca.arnaud.hopsboilingtimer.app.feature.alarm.mapper.AdditionNotificationModelMapper
import ca.arnaud.hopsboilingtimer.app.feature.alarm.model.AdditionNotificationModel
import ca.arnaud.hopsboilingtimer.app.feature.alarm.worker.AdditionNotificationWorker
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeNextAdditionAlert
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdditionAlarmScheduler @Inject constructor(
    coroutineScopeProvider: CoroutineScopeProvider,
    private val subscribeAdditionSchedule: SubscribeAdditionSchedule,
    private val subscribeNextAdditionAlert: SubscribeNextAdditionAlert,
    private val alarmManager: AlarmManager,
    private val workManager: WorkManager,
    private val context: Context,
    private val additionNotificationModelMapper: AdditionNotificationModelMapper,
    private val additionAlarmWorkerDataMapper: AdditionAlarmWorkerDataMapper,
    private val additionAlertDataMapper: AdditionAlertDataMapper,
) {

    companion object {
        const val USE_WORK_MANAGER = true
    }

    init {
        coroutineScopeProvider.scope.launch {
            subscribeNextAdditionAlert.execute().collect { alert ->
                if (alert != null) {
                    schedule(alert)
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
        if (!USE_WORK_MANAGER) {
            legacyCancelAlarm()
            return
        }

        workManager.cancelAllWork()
    }

    private fun legacyCancelAlarm() {
        val notification = AdditionNotificationModel("", "", 0L)
        AdditionAlarmReceiver.createPendingIntent(context, notification)?.let { pendingIntent ->
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun schedule(alert: AdditionAlert) {
        if (!USE_WORK_MANAGER) {
            val notification = additionNotificationModelMapper.mapTo(alert)
            legacySchedule(notification)
            return
        }

        val additionAlertData = additionAlertDataMapper.mapTo(alert)
        val workRequest = OneTimeWorkRequestBuilder<AdditionNotificationWorker>()
            .setInitialDelay(additionAlertData.duration)
            .setInputData(additionAlarmWorkerDataMapper.mapTo(additionAlertData))
            .build()
        workManager.enqueue(workRequest)
    }

    private fun legacySchedule(notification: AdditionNotificationModel) {
        AdditionAlarmReceiver.createPendingIntent(context, notification)?.let { pendingIntent ->
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                notification.triggerAtMillis,
                pendingIntent
            )
        }
    }
}