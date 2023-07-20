package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.factory

import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionTimerScreenModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.BottomBarModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.NewAdditionModel
import ca.arnaud.hopsboilingtimer.app.feature.common.model.TimeButtonModel
import ca.arnaud.hopsboilingtimer.app.feature.common.model.TimeButtonStyle
import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import ca.arnaud.hopsboilingtimer.app.formatter.time.RemainingTimeTextFormatter
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.Duration
import javax.inject.Inject

class AdditionTimerScreenModelFactory @Inject constructor(
    private val rowModelFactory: RowModelFactory,
    private val timeProvider: TimeProvider,
    private val durationTextFormatter: DurationTextFormatter,
    private val remainingTimeTextFormatter: RemainingTimeTextFormatter,
) {

    fun create(
        additions: List<Addition>,
        schedule: AdditionSchedule?,
        newAdditionModel: NewAdditionModel = NewAdditionModel(),
    ): AdditionTimerScreenModel {
        val additionRows = when (schedule) {
            null -> additions.map { rowModelFactory.create(it) }
            else -> schedule.alerts.map {
                val currentAlert = schedule.getNextAlert(timeProvider.getNowLocalDateTime())
                rowModelFactory.create(it, currentAlert)
            }
        }

        return AdditionTimerScreenModel.Edit(
            additionRows = additionRows,
            newAdditionRow = when (schedule) {
                null -> newAdditionModel
                else -> null
            },
            bottomBarModel = createBottomModel(schedule, additions)
        )
    }

    private fun createBottomModel(
        schedule: AdditionSchedule?,
        additions: List<Addition>,
    ): BottomBarModel {
        val maxDuration = additions.maxOfOrNull { it.duration }
        return when (schedule) {
            null -> BottomBarModel(
                timeButton = TimeButtonModel(
                    title = "Start Timer", // TODO - hardcoded string
                    time = maxDuration?.let { duration ->
                        durationTextFormatter.format(duration)
                    } ?: "",
                    style = TimeButtonStyle.Start,
                    enabled = additions.isNotEmpty(),
                ),
            )

            else -> {
                val now = timeProvider.getNowLocalDateTime()
                val triggerAtTime = schedule.alerts.maxOfOrNull { it.triggerAtTime }
                val remainingDuration = triggerAtTime?.let { time ->
                    Duration.between(now, time)
                }
                BottomBarModel(
                    timeButton = TimeButtonModel(
                        title = "Stop Timer", // TODO - hardcoded string
                        time = remainingDuration?.let { duration ->
                            remainingTimeTextFormatter.format(duration)
                        } ?: "",
                        style = TimeButtonStyle.Stop,
                        enabled = true,
                    )
                )
            }
        }
    }
}