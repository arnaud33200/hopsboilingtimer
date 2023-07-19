package ca.arnaud.hopsboilingtimer.app.feature.alert.view

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import ca.arnaud.hopsboilingtimer.R
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.AdditionAlertNotificationModel

class AdditionAlertNotificationView(context: Context) : RemoteViews(
    context.packageName,
    R.layout.view_addition_alert_notification
) {

    fun bind(model: AdditionAlertNotificationModel, context: Context) {

        // TODO - figure out why it doesn't work
//        val serviceIntent = AlertRowRemoteViewsService.createInstance(context, model.rows)
//        setRemoteAdapter(R.id.list_view, serviceIntent)

        // as mentioned in the layout, this is a bad practice
        // temporary solution until I figured out setRemoteAdapter
        for (i in 0..1) {
            when (val rowModel = model.rows.getOrNull(i)) {
                null -> setViewVisibility(
                    when (i) {
                        0 -> R.id.row_layout_coming
                        else -> R.id.row_layout_after
                    }, View.GONE
                )

                else -> {
                    setTextViewText(
                        when (i) {
                            0 -> R.id.title_text_view
                            else -> R.id.title_text_view_after
                        }, rowModel.title
                    )
                    setTextViewText(
                        when (i) {
                            0 -> R.id.duration_text_view
                            else -> R.id.duration_text_view_after
                        }, rowModel.time
                    )
                }
            }
        }
    }
}