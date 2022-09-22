package ca.arnaud.hopsboilingtimer.app.factory

import ca.arnaud.hopsboilingtimer.app.mapper.DurationTextMapper
import ca.arnaud.hopsboilingtimer.app.mapper.RemainingTimeTextMapper
import ca.arnaud.hopsboilingtimer.app.model.AdditionOptionType
import ca.arnaud.hopsboilingtimer.app.model.AdditionRowModel
import ca.arnaud.hopsboilingtimer.domain.model.*
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class AdditionRowModelFactory @Inject constructor(
    private val durationTextMapper: DurationTextMapper,
    private val timeProvider: TimeProvider,
    private val remainingTimeTextMapper: RemainingTimeTextMapper
) {
    enum class AdditionRowMode {
        Edit, Schedule
    }

    fun create(input: Addition, mode: AdditionRowMode, schedule: AdditionSchedule?): AdditionRowModel {
        val options = when (mode) {
            AdditionRowMode.Edit -> listOf(AdditionOptionType.Delete)
            AdditionRowMode.Schedule -> emptyList()
        }
        return AdditionRowModel(
            id = input.id,
            title = input.name,
            duration = mapDuration(input, mode, schedule),
            options = options
        )
    }

    private fun mapDuration(
        addition: Addition,
        mode: AdditionRowMode,
        schedule: AdditionSchedule?
    ): String {
        return when (mode) {
            AdditionRowMode.Edit -> {
                durationTextMapper.mapTo(addition.duration)
            }
            AdditionRowMode.Schedule -> {
                mapScheduleModeDuration(addition, schedule)
            }
        }
    }

    private fun mapScheduleModeDuration(addition: Addition, schedule: AdditionSchedule?): String {
        // TODO - better to just map schedule to rows
        val nowTime = timeProvider.getNowTimeMillis()
        val nextAlert = schedule?.getNextAlert(nowTime)
        val alert = schedule?.alerts?.find { alert ->
            alert.additionsOrEmpty().find { it.id == addition.id } != null
        }

        return when {
            alert == null -> {
                durationTextMapper.mapTo(addition.duration)
            }
            // In Progress
            alert == nextAlert -> {
                val remainingDuration = (alert.triggerAtTime - nowTime).milliseconds
                remainingTimeTextMapper.mapTo(remainingDuration)
            }
            // Expired
            alert.triggerAtTime < nowTime -> {
                // TODO - return a model with a expired mode
                "--:--"
            }
            else -> {
                durationTextMapper.mapTo(addition.duration)
            }
        }
    }

}