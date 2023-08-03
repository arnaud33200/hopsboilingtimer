package ca.arnaud.hopsboilingtimer.app.feature.alert

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertDataFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertWorkerDataMapper
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.worker.AdditionNotificationWorker
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeNextAdditionAlert
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdditionAlertScheduler @Inject constructor(
    private val coroutineScopeProvider: CoroutineScopeProvider,
    private val subscribeAdditionSchedule: SubscribeAdditionSchedule,
    private val subscribeNextAdditionAlert: SubscribeNextAdditionAlert,
    private val workManager: WorkManager,
    private val additionAlertNotificationPresenter: AdditionAlertNotificationPresenter,
    private val additionAlertWorkerDataMapper: AdditionAlertWorkerDataMapper,
    private val additionAlertDataFactory: AdditionAlertDataFactory,
) {
    private var currentSchedule: ScheduleStatus? = null

    init {
        coroutineScopeProvider.scope.launch {
            subscribeNextAdditionAlert.execute().collect { alert ->
                schedule(alert)
            }
        }
        coroutineScopeProvider.scope.launch {
            subscribeAdditionSchedule.execute().collect { status ->
                onScheduleStatusUpdate(status)
            }
        }
    }

    private fun onScheduleStatusUpdate(status: ScheduleStatus) {
        when (status) {
            is ScheduleStatus.Started -> {} // No-op
            ScheduleStatus.Stopped -> {
                if (currentSchedule is ScheduleStatus.Started) {
                    additionAlertNotificationPresenter.showEnd()
                } else {
                    cancelAlarm()
                }
            }

            ScheduleStatus.Iddle,
            ScheduleStatus.Canceled -> cancelAlarm()
        }
        currentSchedule = status
    }

    private fun cancelAlarm() {
        additionAlertNotificationPresenter.cancel()
        workManager.cancelAllWork()
    }

    private fun schedule(alert: AdditionAlert?) {
        if (alert == null) {
            return
        }

        val additionAlertData = additionAlertDataFactory.create(alert)
        schedule(additionAlertData)

        if (additionAlertData.initialDelay >= Duration.ofSeconds(50)) {
            val reminderAlertData = additionAlertDataFactory.createReminder(additionAlertData)
            schedule(reminderAlertData)
        }
    }

    private fun schedule(additionAlertData: AdditionAlertData) {
        val workRequest = OneTimeWorkRequestBuilder<AdditionNotificationWorker>()
            .setInitialDelay(additionAlertData.initialDelay)
            .setInputData(additionAlertWorkerDataMapper.mapTo(additionAlertData))
            .build()
        workManager.enqueue(workRequest)
    }
}