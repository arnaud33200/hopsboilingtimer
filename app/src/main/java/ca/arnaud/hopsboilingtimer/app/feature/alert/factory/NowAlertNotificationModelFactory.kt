package ca.arnaud.hopsboilingtimer.app.feature.alert.factory

import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertData
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.NowAlertNotificationModel
import ca.arnaud.hopsboilingtimer.app.formatter.time.DurationTextFormatter
import ca.arnaud.hopsboilingtimer.app.provider.StringProvider
import ca.arnaud.hopsboilingtimer.domain.model.AdditionAlert
import ca.arnaud.hopsboilingtimer.domain.model.additionsOrEmpty
import ca.arnaud.hopsboilingtimer.domain.model.getDuration
import ca.arnaud.hopsboilingtimer.domain.model.schedule.AdditionSchedule
import java.time.Duration
import javax.inject.Inject

class NowAlertNotificationModelFactory @Inject constructor(
    private val stringProvider: StringProvider,
    private val durationTextFormatter: DurationTextFormatter,
) {

    fun createAddHops(
        currentAlertData: AdditionAlertData,
        schedule: AdditionSchedule?,
    ): NowAlertNotificationModel? {
        val currentAlert = schedule?.alerts?.find { alert ->
            alert.id == currentAlertData.id
        } ?: return null

        return NowAlertNotificationModel(
            title = stringProvider.get(R.string.notification_now_add_hops_title),
            text = getHopsListText(currentAlert)
        )
    }

    fun createEnd(): NowAlertNotificationModel {
        return NowAlertNotificationModel(
            title = stringProvider.get(R.string.notification_stop_boiling)
        )
    }

    private fun getHopsListText(alert: AdditionAlert): String {
        val additions = alert.additionsOrEmpty()
        val duration = alert.getDuration() ?: Duration.ZERO
        val hops = additions.joinToString(separator = ", ", prefix = "") { it.name }
        return stringProvider.get(
            R.string.notification_now_add_hops_text,
            hops,
            durationTextFormatter.format(duration),
        )
    }
}