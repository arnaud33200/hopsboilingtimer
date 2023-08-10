package ca.arnaud.hopsboilingtimer.app.feature.alert.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.feature.alert.AdditionAlertNotificationPresenter
import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertWorkerDataMapper
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertDataType
import ca.arnaud.hopsboilingtimer.domain.usecase.alert.OnAdditionAlertReceived
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.GetAdditionSchedule
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

@HiltWorker
class AdditionNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val coroutineScopeProvider: CoroutineScopeProvider,
    private val onAdditionAlertReceived: OnAdditionAlertReceived,
    private val getAdditionSchedule: GetAdditionSchedule,
    private val additionAlertWorkerDataMapper: AdditionAlertWorkerDataMapper,
    private val additionAlertNotificationPresenter: AdditionAlertNotificationPresenter,
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        coroutineScopeProvider.scope.launch {
            val alertData = additionAlertWorkerDataMapper.mapFrom(inputData)
            val schedule = getAdditionSchedule.execute()
            additionAlertNotificationPresenter.showNextAlerts(alertData, schedule, context)
            additionAlertNotificationPresenter.showNowAlert(alertData, schedule, context)

            when (alertData.type) {
                AdditionAlertDataType.Alert -> {
                    val params = OnAdditionAlertReceived.Params(alertData.id)
                    onAdditionAlertReceived.execute(params)
                }

                AdditionAlertDataType.Reminder -> {} // No-op
            }

        }

        return Result.success()
    }
}
