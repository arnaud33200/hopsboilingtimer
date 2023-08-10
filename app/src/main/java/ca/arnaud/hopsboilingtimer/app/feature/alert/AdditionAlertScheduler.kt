package ca.arnaud.hopsboilingtimer.app.feature.alert

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertDataFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertWorkerDataMapper
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.worker.AdditionNotificationWorker
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.statemachine.schedule.AdditionScheduleState
import ca.arnaud.hopsboilingtimer.domain.usecase.alert.SubscribeNextAdditionAlert
import ca.arnaud.hopsboilingtimer.domain.usecase.schedulestate.SubscribeScheduleState
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdditionAlertScheduler @Inject constructor(
    coroutineScopeProvider: CoroutineScopeProvider,
    private val subscribeScheduleState: SubscribeScheduleState,
    private val subscribeNextAdditionAlert: SubscribeNextAdditionAlert,
    private val workManager: WorkManager,
    private val additionAlertNotificationPresenter: AdditionAlertNotificationPresenter,
    private val additionAlertWorkerDataMapper: AdditionAlertWorkerDataMapper,
    private val additionAlertDataFactory: AdditionAlertDataFactory,
) {
    private var currentSchedule: AdditionScheduleState? = null

    init {
        coroutineScopeProvider.scope.launch {
            subscribeNextAdditionAlert.execute().collect { alert ->
                schedule(alert)
            }
        }
        coroutineScopeProvider.scope.launch {
            subscribeScheduleState.execute().collect { status ->
                onScheduleStatusUpdate(status)
            }
        }
    }

    private fun onScheduleStatusUpdate(status: AdditionScheduleState) {
        when (status) {
            is AdditionScheduleState.Started -> {} // No-op
            AdditionScheduleState.Stopped -> {
                if (currentSchedule is AdditionScheduleState.Started) {
                    additionAlertNotificationPresenter.showEndAlert()
                } else {
                    cancelAlarm()
                }
            }

            AdditionScheduleState.Idle,
            AdditionScheduleState.Canceled -> cancelAlarm()
        }
        currentSchedule = status
    }

    private fun cancelAlarm() {
        additionAlertNotificationPresenter.hideNotifications()
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