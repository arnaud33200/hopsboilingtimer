package ca.arnaud.hopsboilingtimer.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ScheduleEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "start_time") val startTime: Long,
    @ColumnInfo(name = "alert_ids") val alertIds: List<String>
)