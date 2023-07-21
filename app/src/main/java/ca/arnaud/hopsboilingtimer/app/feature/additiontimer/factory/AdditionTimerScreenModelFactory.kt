package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.factory

import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionTimerScreenModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AlertRowModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.BottomBarModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.NewAdditionModel
import ca.arnaud.hopsboilingtimer.app.feature.common.model.TimeButtonModel
import ca.arnaud.hopsboilingtimer.app.feature.common.model.TimeButtonStyle
import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import ca.arnaud.hopsboilingtimer.app.formatter.time.RemainingTimeTextFormatter
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.Duration
import javax.inject.Inject

class AdditionTimerScreenModelFactory @Inject constructor(
    private val additionRowModelFactory: AdditionRowModelFactory,
    private val alertRowModelFactory: AlertRowModelFactory,
    private val timeProvider: TimeProvider,
    private val durationTextFormatter: DurationTextFormatter,
    private val remainingTimeTextFormatter: RemainingTimeTextFormatter,
) {

    fun create(
        additions: List<Addition>,
        schedule: AdditionSchedule?,
        newAdditionModel: NewAdditionModel = NewAdditionModel(),
    ): AdditionTimerScreenModel {
        val bottomBarModel = createBottomModel(schedule, additions)

        return when (schedule) {
            null -> AdditionTimerScreenModel.Edit(
                additionRows = additions.map { additionRowModelFactory.create(it) },
                newAdditionRow = newAdditionModel,
                bottomBarModel = bottomBarModel
            )

            else -> {
                val rows = schedule.alerts.map { additionAlert ->
                    val currentAlert = schedule.getNextAlert(timeProvider.getNowLocalDateTime())
                    alertRowModelFactory.create(additionAlert, currentAlert)
                }
                AdditionTimerScreenModel.Schedule(
                    nextRows = rows.filterIsInstance(AlertRowModel.Next::class.java),
                    addedRows = rows.filterIsInstance(AlertRowModel.Added::class.java),
                    bottomBarModel = createBottomModel(schedule, additions)
                )
            }
        }
    }

    fun getButtonTime(schedule: AdditionSchedule?): String {
        val now = timeProvider.getNowLocalDateTime()
        val triggerAtTime = schedule?.alerts?.maxOfOrNull { it.triggerAtTime }
        val remainingDuration = triggerAtTime?.let { time ->
            Duration.between(now, time)
        }

        return remainingDuration?.let { duration ->
            remainingTimeTextFormatter.format(duration)
        } ?: ""
    }

    fun getHighlightedTimeText(alert: AdditionAlert): String {
        return alertRowModelFactory.getHighlightedTimeText(alert)
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
                BottomBarModel(
                    timeButton = TimeButtonModel(
                        title = "Stop Timer", // TODO - hardcoded string
                        time = getButtonTime(schedule),
                        style = TimeButtonStyle.Stop,
                        enabled = true,
                    )
                )
            }
        }
    }
}