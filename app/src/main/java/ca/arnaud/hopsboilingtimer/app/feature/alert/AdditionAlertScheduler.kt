package ca.arnaud.hopsboilingtimer.app.feature.alert

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertDataFactory
import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertWorkerDataMapper
import ca.arnaud.hopsboilingtimer.app.feature.alert.worker.AdditionNotificationWorker
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.ScheduleStatus
import ca.arnaud.hopsboilingtimer.domain.model.schedule.getSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeNextAdditionAlert
import kotlinx.coroutines.launch
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

    init {
        coroutineScopeProvider.scope.launch {
            subscribeNextAdditionAlert.execute().collect { alert ->
                if (alert != null) {
                    schedule(alert)
                }
            }
        }
        coroutineScopeProvider.scope.launch {
            subscribeAdditionSchedule.execute().collect { status ->
                when (status) {
                    ScheduleStatus.Canceled -> cancelAlarm()
                    is ScheduleStatus.Started -> {} // No-op
                    ScheduleStatus.Stopped -> cancelAlarm()
                }
                if (status.getSchedule() == null) {
                    cancelAlarm()
                }
            }
        }
    }

    private fun cancelAlarm() {
        additionAlertNotificationPresenter.cancel()
        workManager.cancelAllWork()
    }

    private fun schedule(alert: AdditionAlert) {
        val additionAlertData = additionAlertDataFactory.create(alert)
        val workRequest = OneTimeWorkRequestBuilder<AdditionNotificationWorker>()
            .setInitialDelay(additionAlertData.initialDelay)
            .setInputData(additionAlertWorkerDataMapper.mapTo(additionAlertData))
            .build()
        workManager.enqueue(workRequest)
    }
}