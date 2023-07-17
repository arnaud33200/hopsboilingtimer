package ca.arnaud.hopsboilingtimer.app.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.work.WorkManager
import ca.arnaud.hopsboilingtimer.app.executor.JobExecutorProviderImpl
import ca.arnaud.hopsboilingtimer.app.provider.TimeProviderImpl
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object {

        @Provides
        fun provideContext(application: Application): Context {
            return application.applicationContext
        }

        @Provides
        @Singleton
        fun provideWorkManager(context: Context) : WorkManager {
            return WorkManager.getInstance(context)
        }

        @Provides
        @Singleton
        fun provideNotificationManager(context: Context) : NotificationManager {
            return ContextCompat.getSystemService(
                context, NotificationManager::class.java
            ) as NotificationManager
        }
    }

    @Binds
    abstract fun bindJobExecutorProvider(impl: JobExecutorProviderImpl): JobExecutorProvider

    @Binds
    abstract fun bindTimeProvider(impl: TimeProviderImpl): TimeProvider
}