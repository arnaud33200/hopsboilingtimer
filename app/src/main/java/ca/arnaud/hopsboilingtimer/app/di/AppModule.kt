package ca.arnaud.hopsboilingtimer.app.di

import ca.arnaud.hopsboilingtimer.app.executor.JobExecutorProviderImpl
import ca.arnaud.hopsboilingtimer.domain.usecase.common.JobExecutorProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindJobExecutorProvider(impl: JobExecutorProviderImpl): JobExecutorProvider
}