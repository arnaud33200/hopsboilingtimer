package ca.arnaud.hopsboilingtimer.app.feature.alert.view

import android.widget.RemoteViews
import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertNotificationModel
import ca.arnaud.hopsboilingtimer.app.feature.alert.view.AdditionAlertNotificationViewConfig.PACKAGE_NAME

object AdditionAlertNotificationViewConfig {
    const val PACKAGE_NAME = "ca.arnaud.hopsboilingtimer"
}

class AdditionAlertNotificationView : RemoteViews(
    PACKAGE_NAME,
    R.layout.view_addition_alert_notification
) {

    fun bind(model: AdditionAlertNotificationModel) {
        setTextViewText(R.id.title_text_view, model.title)
        setTextViewText(R.id.duration_text_view, model.duration)
    }
}