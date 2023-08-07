package ca.arnaud.hopsboilingtimer.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import ca.arnaud.hopsboilingtimer.app.feature.alert.AdditionAlertScheduler
import ca.arnaud.hopsboilingtimer.domain.usecase.schedulestate.InitializeScheduleState
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidApp
class HopsBoilingTimerApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var additionAlertScheduler: AdditionAlertScheduler

    @Inject
    @Named("App")
    lateinit var applicationScope: CoroutineScope

    @Inject
    lateinit var initializeScheduleState: InitializeScheduleState

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch {
            initializeScheduleState.execute(Unit)
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder().setWorkerFactory(
        EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory()
    ).build()
}
