package ca.arnaud.hopsboilingtimer.app.feature.alert

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertWorkerDataMapper
import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertDataMapper
import ca.arnaud.hopsboilingtimer.app.feature.alert.worker.AdditionNotificationWorker
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeAdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.SubscribeNextAdditionAlert
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdditionAlertScheduler @Inject constructor(
    coroutineScopeProvider: CoroutineScopeProvider,
    private val subscribeAdditionSchedule: SubscribeAdditionSchedule,
    private val subscribeNextAdditionAlert: SubscribeNextAdditionAlert,
    private val workManager: WorkManager,
    private val additionAlertWorkerDataMapper: AdditionAlertWorkerDataMapper,
    private val additionAlertDataMapper: AdditionAlertDataMapper,
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
            subscribeAdditionSchedule.execute().collect { schedule ->
                if (schedule == null) {
                    cancelAlarm()
                }
            }
        }
    }

    private fun cancelAlarm() {
        workManager.cancelAllWork()
    }

    private fun schedule(alert: AdditionAlert) {
        val additionAlertData = additionAlertDataMapper.mapTo(alert)
        val workRequest = OneTimeWorkRequestBuilder<AdditionNotificationWorker>()
            .setInitialDelay(additionAlertData.initialDelay)
            .setInputData(additionAlertWorkerDataMapper.mapTo(additionAlertData))
            .build()
        workManager.enqueue(workRequest)
    }
}