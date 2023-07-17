package ca.arnaud.hopsboilingtimer.app.feature.alarm.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import ca.arnaud.hopsboilingtimer.app.executor.CoroutineScopeProvider
import ca.arnaud.hopsboilingtimer.app.feature.alarm.AdditionNotificationPresenter
import ca.arnaud.hopsboilingtimer.app.feature.alarm.mapper.AdditionAlarmWorkerDataMapper
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
    private val additionAlarmWorkerDataMapper: AdditionAlarmWorkerDataMapper,
    private val additionNotificationPresenter: AdditionNotificationPresenter,
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val additionAlertData = additionAlarmWorkerDataMapper.mapFrom(inputData)
        additionNotificationPresenter.show(additionAlertData, context)

        coroutineScopeProvider.scope.launch {
            onAdditionAlertReceived.execute(OnAdditionAlertReceived.Params(additionAlertData.id))
        }

        return Result.success()
    }
}
