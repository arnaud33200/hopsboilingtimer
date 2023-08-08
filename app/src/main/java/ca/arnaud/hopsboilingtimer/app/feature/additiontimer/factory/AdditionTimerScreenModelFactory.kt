package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.factory

import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AdditionTimerScreenModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AlertRowModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.BottomBarModel
import ca.arnaud.hopsboilingtimer.app.feature.common.model.TimeButtonModel
import ca.arnaud.hopsboilingtimer.app.feature.common.model.TimeButtonStyle
import ca.arnaud.hopsboilingtimer.app.formatter.time.CountdownTimerTextFormatter
import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import ca.arnaud.hopsboilingtimer.app.provider.StringProvider
import ca.arnaud.hopsboilingtimer.domain.model.Addition
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import ca.arnaud.hopsboilingtimer.domain.model.schedule.getNextAlert
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.Duration
import javax.inject.Inject

class AdditionTimerScreenModelFactory @Inject constructor(
    private val timeProvider: TimeProvider,
    private val stringProvider: StringProvider,
    private val additionRowModelFactory: AdditionRowModelFactory,
    private val alertRowModelFactory: AlertRowModelFactory,
    private val durationTextFormatter: DurationTextFormatter,
    private val countdownTimerTextFormatter: CountdownTimerTextFormatter,
) {

    fun create(
        additions: List<Addition>,
        schedule: AdditionSchedule?,
    ): AdditionTimerScreenModel {
        val bottomBarModel = createBottomModel(schedule, additions)

        return when (schedule) {
            null -> AdditionTimerScreenModel.Edit(
                additionRows = additions.map { additionRowModelFactory.create(it) },
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
            countdownTimerTextFormatter.format(duration)
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
                    title = stringProvider.get(R.string.addition_timer_start_button),
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
                        title = stringProvider.get(R.string.addition_timer_stop_button),
                        time = getButtonTime(schedule),
                        style = TimeButtonStyle.Stop,
                        enabled = true,
                    )
                )
            }
        }
    }
}