package ca.arnaud.hopsboilingtimer.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlertEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "type") val type: AlertTypeEntity,
    @ColumnInfo(name = "trigger_at") val triggerAt: Long,
    @ColumnInfo(name = "addition_ids") val additionIds: List<String> // only for type Start & Next
)

enum class AlertTypeEntity {
    Start, Next, End
}