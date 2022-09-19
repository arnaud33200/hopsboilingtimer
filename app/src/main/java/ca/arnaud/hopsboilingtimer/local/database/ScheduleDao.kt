package ca.arnaud.hopsboilingtimer.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ca.arnaud.hopsboilingtimer.local.entity.AlertEntity
import ca.arnaud.hopsboilingtimer.local.entity.ScheduleEntity

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM scheduleentity")
    fun getSchedules(): List<ScheduleEntity>

    @Query("SELECT * FROM alertentity WHERE id = :alertId")
    fun getAlert(alertId: String): AlertEntity?

    @Insert
    fun insertSchedule(scheduleEntity: ScheduleEntity)

    @Insert
    fun insertAlert(alertEntity: AlertEntity)

    @Query("DELETE FROM scheduleentity WHERE id = :scheduleId")
    fun deleteAddition(scheduleId: String)
}