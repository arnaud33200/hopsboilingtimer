package ca.arnaud.hopsboilingtimer.app.factory

import ca.arnaud.hopsboilingtimer.app.mapper.DurationTextMapper
import ca.arnaud.hopsboilingtimer.app.mapper.RemainingTimeTextMapper
import ca.arnaud.hopsboilingtimer.app.model.AdditionOptionType
import ca.arnaud.hopsboilingtimer.app.model.RowModel
import ca.arnaud.hopsboilingtimer.domain.model.*
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class RowModelFactory @Inject constructor(
    private val durationTextMapper: DurationTextMapper,
    private val timeProvider: TimeProvider,
    private val remainingTimeTextMapper: RemainingTimeTextMapper
) {
    enum class AdditionRowMode {
        Edit, Schedule
    }

    fun create(addition: Addition): RowModel {
        return RowModel.AdditionRowModel(
            id = addition.id,
            title = addition.name,
            duration = durationTextMapper.mapTo(addition.duration),
            options = listOf(AdditionOptionType.Delete)
        )
    }

    fun create(alert: AdditionAlert): RowModel {
        val nowTime = timeProvider.getNowTimeMillis()
        val expired = alert.triggerAtTime < nowTime
        if (alert is AdditionAlert.End) {
            val remainingDuration = (alert.triggerAtTime - nowTime).milliseconds
            return RowModel.AlertRowModel(
                id = alert.id,
                title = "",
                duration = "END in ${remainingTimeTextMapper.mapTo(remainingDuration)}",
                disabled = expired
            )
        }

        val title = alert.additionsOrEmpty().joinToString(separator = ", ") { it.name }
        val countdown  = when {
             expired -> "Added!"
            else -> {
                val remainingDuration = (alert.triggerAtTime - nowTime).milliseconds
                "Add in ${remainingTimeTextMapper.mapTo(remainingDuration)}" // TODO - Hardcoded string
            }
        }

        val duration = alert.additionsOrEmpty().firstOrNull()?.let { addition ->
            durationTextMapper.mapTo(addition.duration)
        } ?: ""
        return RowModel.AlertRowModel(
            id = alert.id,
            title = "$title ($duration)",
            duration = countdown,
            disabled = expired
        )
    }
}