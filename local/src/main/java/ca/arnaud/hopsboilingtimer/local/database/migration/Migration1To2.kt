package ca.arnaud.hopsboilingtimer.local.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_TO_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // "pause_time" column added
        database.execSQL("ALTER TABLE ScheduleEntity ADD COLUMN pause_time INTEGER NOT NULL DEFAULT 0")
    }
}