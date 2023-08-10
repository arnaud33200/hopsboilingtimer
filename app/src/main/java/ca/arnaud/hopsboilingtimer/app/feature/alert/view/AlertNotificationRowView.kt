package ca.arnaud.hopsboilingtimer.app.feature.alert.view

import android.content.Context
import android.widget.RemoteViews
import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.NextAlertNotificationRowModel

class AlertNotificationRowView(context: Context) : RemoteViews(
    context.packageName,
    R.layout.view_row_addition_alert_notification
) {

    fun bind(model: NextAlertNotificationRowModel) {
        setTextViewText(R.id.title_text_view, model.title)
        setTextViewText(R.id.duration_text_view, model.time)
    }
}