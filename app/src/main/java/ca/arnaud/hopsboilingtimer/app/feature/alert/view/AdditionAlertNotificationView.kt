package ca.arnaud.hopsboilingtimer.app.feature.alert.view

import android.content.Context
import android.widget.RemoteViews
import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertNotificationModel

class AdditionAlertNotificationView(context: Context) : RemoteViews(
    context.packageName,
    R.layout.view_addition_alert_notification
) {

    fun bind(model: AdditionAlertNotificationModel, context: Context) {
        val serviceIntent = AlertRowRemoteViewsService.createInstance(context, model.rows)
        setRemoteAdapter(R.id.list_view, serviceIntent)
    }
}