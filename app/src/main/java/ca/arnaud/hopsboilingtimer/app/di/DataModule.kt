package ca.arnaud.hopsboilingtimer.app.di

import ca.arnaud.hopsboilingtimer.data.repository.AdditionRepositoryImpl
import ca.arnaud.hopsboilingtimer.domain.repository.AdditionRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    abstract fun bindAdditionRepository(impl: AdditionRepositoryImpl): AdditionRepository
}