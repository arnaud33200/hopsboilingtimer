package ca.arnaud.hopsboilingtimer.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration

@Entity
data class AlertEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "type") val type: AlertTypeEntity,
    @ColumnInfo(name = "trigger_at") val triggerAt: Long, // TODO - use string and format with ISO
    @ColumnInfo(name = "addition_ids") val additionIds: List<String>, // only for type Start & Next
    @ColumnInfo(name = "duration") val duration: Duration, // only for type End
    @ColumnInfo(name = "checked") val checked: Boolean
)

enum class AlertTypeEntity {
    Start, Next, End
}