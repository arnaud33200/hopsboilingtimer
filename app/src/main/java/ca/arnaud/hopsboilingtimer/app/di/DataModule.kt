package ca.arnaud.hopsboilingtimer.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import ca.arnaud.hopsboilingtimer.data.datasource.AdditionLocalDataSource
import ca.arnaud.hopsboilingtimer.data.datasource.ScheduleLocalDataSource
import ca.arnaud.hopsboilingtimer.data.repository.AdditionRepositoryImpl
import ca.arnaud.hopsboilingtimer.data.repository.ScheduleRepositoryImpl
import ca.arnaud.hopsboilingtimer.domain.repository.AdditionRepository
import ca.arnaud.hopsboilingtimer.domain.repository.ScheduleRepository
import ca.arnaud.hopsboilingtimer.local.AdditionLocalDataSourceImpl
import ca.arnaud.hopsboilingtimer.local.AppDatabase
import ca.arnaud.hopsboilingtimer.local.ScheduleLocalDataSourceImpl
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
        const val PREFERENCES_DATASTORE_NAME = "datastore_preferences"

        @Provides
        fun provideAppDataBase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATA_BASE_NAME
            ).build()
        }

        @Provides
        @Singleton
        fun providesDataStore(context: Context): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create(produceFile = {
                context.preferencesDataStoreFile(PREFERENCES_DATASTORE_NAME)
            })
        }
    }

    @Binds
    @Singleton
    abstract fun bindAdditionRepository(impl: AdditionRepositoryImpl): AdditionRepository

    @Binds
    @Singleton
    abstract fun bindScheduleRepository(impl: ScheduleRepositoryImpl): ScheduleRepository

    @Binds
    @Singleton
    abstract fun bindAdditionLocalDataSource(impl: AdditionLocalDataSourceImpl): AdditionLocalDataSource

    @Binds
    @Singleton
    abstract fun bindScheduleLocalDataSource(impl: ScheduleLocalDataSourceImpl): ScheduleLocalDataSource
}