package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.factory

import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model.AlertRowModel
import ca.arnaud.hopsboilingtimer.app.formatter.time.CountdownTimerTextFormatter
import ca.arnaud.hopsboilingtimer.app.formatter.time.TimeHoursTextFormatter
import ca.arnaud.hopsboilingtimer.app.provider.StringProvider
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.additionsOrEmpty
import ca.arnaud.hopsboilingtimer.domain.model.isChecked
import ca.arnaud.hopsboilingtimer.domain.provider.TimeProvider
import java.time.Duration
import javax.inject.Inject

class AlertRowModelFactory @Inject constructor(
    private val timeProvider: TimeProvider,
    private val stringProvider: StringProvider,
    private val countdownTimerTextFormatter: CountdownTimerTextFormatter,
    private val timeHoursTextFormatter: TimeHoursTextFormatter,
) {
    fun create(
        alert: AdditionAlert,
        currentAlert: AdditionAlert?,
    ): AlertRowModel {
        val remainingDuration = getRemainingDuration(alert)
        val highlighted = alert == currentAlert

        return when {
            remainingDuration.isNegative -> createAdded(alert)
            else -> createNext(alert, highlighted, remainingDuration)
        }
    }

    private fun createNext(
        alert: AdditionAlert,
        highlighted: Boolean,
        remainingDuration: Duration
    ): AlertRowModel.Next {
        return AlertRowModel.Next(
            id = alert.id,
            title = getTitle(alert),
            time = when {
                highlighted -> getHighlightedTimeText(remainingDuration)
                else -> stringProvider.get(
                    R.string.time_at_time,
                    timeHoursTextFormatter.format(alert.triggerAtTime)
                )
            },
            highlighted = highlighted,
        )
    }

    fun getHighlightedTimeText(alert: AdditionAlert): String {
        val remainingDuration = getRemainingDuration(alert)
        return getHighlightedTimeText(remainingDuration)
    }

    private fun getRemainingDuration(alert: AdditionAlert): Duration {
        val nowTime = timeProvider.getNowLocalDateTime()
        return Duration.between(nowTime, alert.triggerAtTime)
    }

    private fun getHighlightedTimeText(remainingDuration: Duration): String {
        return stringProvider.get(
            R.string.time_in_countdown,
            countdownTimerTextFormatter.format(remainingDuration),
        )
    }

    private fun createAdded(alert: AdditionAlert): AlertRowModel.Added {
        return AlertRowModel.Added(
            id = alert.id,
            title = getTitle(alert),
            time = timeHoursTextFormatter.format(alert.triggerAtTime),
            checked = alert.isChecked() ?: false,
        )
    }

    private fun getTitle(alert: AdditionAlert): String {
        return when (alert) {
            is AdditionAlert.End -> stringProvider.get(R.string.alert_row_end_boiling)
            is AdditionAlert.Next,
            is AdditionAlert.Start -> {
                alert.additionsOrEmpty().joinToString(separator = ", ") { it.name }
                    .takeIf { it.isNotBlank() }?.let { additions ->
                        "+ $additions"
                    } ?: ""
            }
        }
    }

}