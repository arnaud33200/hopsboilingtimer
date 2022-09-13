package ca.arnaud.hopsboilingtimer.app.di

import android.content.Context
import androidx.room.Room
import ca.arnaud.hopsboilingtimer.data.datasource.AdditionLocalDataSource
import ca.arnaud.hopsboilingtimer.data.repository.AdditionRepositoryImpl
import ca.arnaud.hopsboilingtimer.domain.repository.AdditionRepository
import ca.arnaud.hopsboilingtimer.local.AdditionLocalDataSourceImpl
import ca.arnaud.hopsboilingtimer.local.AppDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    companion object {
        const val DATA_BASE_NAME = "hops_additions"

        @Provides
        fun provideAppDataBase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATA_BASE_NAME
            ).build()
        }
    }

    @Binds
    @Singleton
    abstract fun bindAdditionRepository(impl: AdditionRepositoryImpl): AdditionRepository

    @Binds
    @Singleton
    abstract fun bindAdditionLocalDataSource(impl: AdditionLocalDataSourceImpl): AdditionLocalDataSource
}