package ca.arnaud.hopsboilingtimer.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import ca.arnaud.hopsboilingtimer.app.feature.alarm.AdditionAlarmScheduler
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@HiltAndroidApp
class HopsBoilingTimerApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var additionAlarmScheduler: AdditionAlarmScheduler

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder().setWorkerFactory(
        EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory()
    ).build()
}
