package ca.arnaud.hopsboilingtimer.app.feature.alert.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.feature.alert.AdditionAlertNotificationPresenter
import ca.arnaud.hopsboilingtimer.app.feature.alert.mapper.AdditionAlertWorkerDataMapper
import ca.arnaud.hopsboilingtimer.domain.usecase.schedule.OnAdditionAlertReceived
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

@HiltWorker
class AdditionNotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val onAdditionAlertReceived: OnAdditionAlertReceived,
    private val coroutineScopeProvider: CoroutineScopeProvider,
    private val additionAlertWorkerDataMapper: AdditionAlertWorkerDataMapper,
    private val additionAlertNotificationPresenter: AdditionAlertNotificationPresenter,
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val additionAlertData = additionAlertWorkerDataMapper.mapFrom(inputData)
        additionAlertNotificationPresenter.show(additionAlertData, context)

        coroutineScopeProvider.scope.launch {
            val alertId = additionAlertData.comingAlert.id
            onAdditionAlertReceived.execute(OnAdditionAlertReceived.Params(alertId))
        }

        return Result.success()
    }
}
