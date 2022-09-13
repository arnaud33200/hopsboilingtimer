package ca.arnaud.hopsboilingtimer.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ca.arnaud.hopsboilingtimer.local.entity.AdditionEntity

@Database(entities = [AdditionEntity::class], version = 1)
@TypeConverters(DurationConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun additionDao(): AdditionDao
}