package ca.arnaud.hopsboilingtimer.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ca.arnaud.hopsboilingtimer.local.converter.DurationConverter
import ca.arnaud.hopsboilingtimer.local.converter.StringListConverter
import ca.arnaud.hopsboilingtimer.local.database.AdditionDao
import ca.arnaud.hopsboilingtimer.local.database.ScheduleDao
import ca.arnaud.hopsboilingtimer.local.entity.AdditionEntity
import ca.arnaud.hopsboilingtimer.local.entity.AlertEntity
import ca.arnaud.hopsboilingtimer.local.entity.ScheduleEntity

@Database(
    version = 1,
    entities = [
        AdditionEntity::class,
        ScheduleEntity::class,
        AlertEntity::class
    ]
)
@TypeConverters(
    DurationConverter::class,
    StringListConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun additionDao(): AdditionDao

    abstract fun scheduleDao(): ScheduleDao
}