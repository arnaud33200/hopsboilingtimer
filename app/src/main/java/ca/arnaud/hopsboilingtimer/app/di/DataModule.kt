package ca.arnaud.hopsboilingtimer.app.di

import ca.arnaud.hopsboilingtimer.data.repository.AdditionRepositoryImpl
import ca.arnaud.hopsboilingtimer.domain.repository.AdditionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindAdditionRepository(impl: AdditionRepositoryImpl): AdditionRepository
}