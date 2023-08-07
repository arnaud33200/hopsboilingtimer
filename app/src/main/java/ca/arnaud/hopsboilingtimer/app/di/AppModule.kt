package ca.arnaud.hopsboilingtimer.app.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named
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
        fun provideNotificationManagerCompat(context: Context) : NotificationManagerCompat {
            return NotificationManagerCompat.from(context)
        }

        @Provides
        @Singleton
        fun provideNotificationManager(context: Context): NotificationManager {
            return ContextCompat.getSystemService(
                context, NotificationManager::class.java
            ) as NotificationManager
        }

        @Provides
        @Named("App")
        fun provideApplicationScope(): CoroutineScope {
            return CoroutineScope(Dispatchers.Default + SupervisorJob())
        }

        @Provides
        @Named("InitScheduleState") // TODO - Maybe better to not use names? or at least have the name on a const val
        @Singleton
        fun provideCompletableDeferred(): CompletableDeferred<Unit> {
            return CompletableDeferred<Unit>()
        }
    }

    @Binds
    abstract fun bindJobExecutorProvider(impl: JobExecutorProviderImpl): JobExecutorProvider

    @Binds
    abstract fun bindTimeProvider(impl: TimeProviderImpl): TimeProvider
}