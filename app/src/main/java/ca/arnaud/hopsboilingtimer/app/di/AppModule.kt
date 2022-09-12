package ca.arnaud.hopsboilingtimer.app.di

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import android.content.Context.ALARM_SERVICE
import ca.arnaud.hopsboilingtimer.app.executor.JobExecutorProviderImpl
import ca.arnaud.hopsboilingtimer.app.provider.TimeProviderImpl
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {
        @Provides
        fun provideAlarmManager(application: Application): AlarmManager {
            return application.getSystemService(ALARM_SERVICE) as AlarmManager
        }

        @Provides
        fun provideContext(application: Application): Context {
            return application.applicationContext
        }
    }

    @Binds
    abstract fun bindJobExecutorProvider(impl: JobExecutorProviderImpl): JobExecutorProvider

    @Binds
    abstract fun bindTimeProvider(impl: TimeProviderImpl): TimeProvider
}