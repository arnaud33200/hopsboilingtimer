package ca.arnaud.hopsboilingtimer.app.alarm

import android.app.AlarmManager
import android.content.Context
import ca.arnaud.hopsboilingtimer.app.mapper.AdditionNotificationMapper
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import javax.inject.Inject

class AdditionAlarmScheduler @Inject constructor(
    private val alarmManager: AlarmManager,
    private val context: Context,
    private val additionNotificationMapper: AdditionNotificationMapper
) {

    init {
        // TODO - call GetNextAlarmFlow usecase and schedule
    }

    fun schedule(alerts: List<AdditionAlert>) {
        val additionNotifications = alerts.map { additionNotificationMapper.mapTo(it) }
        scheduleWith(AdditionNotificationSchedule(additionNotifications))
    }

    fun scheduleNext(schedule: AdditionNotificationSchedule) {
        // TODO - replace with the proper usecase call
        val notifications = schedule.AdditionNotifications
        val newNotifications = notifications.subList(0, notifications.lastIndex)
        scheduleWith(schedule.copy(AdditionNotifications = newNotifications))
    }

    private fun scheduleWith(schedule: AdditionNotificationSchedule) {
        val nextNotification = schedule.AdditionNotifications.firstOrNull()
        if (nextNotification == null) {
            return
        }

        val pendingIntent = AdditionAlarmReceiver.createPendingIntent(context, schedule)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            nextNotification.triggerAtMillis,
            pendingIntent
        )
    }
}