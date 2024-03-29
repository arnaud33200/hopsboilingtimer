package ca.arnaud.hopsboilingtimer.app.feature.alert.view

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import ca.arnaud.hopsboilingtimer.app.feature.alert.model.NextAlertNotificationRowModel

class AlertRowRemoteViewsService : RemoteViewsService() {

    companion object {
        private const val ROWS_EXTRA_KEY = "rows"

        fun createInstance(context: Context, rows: List<NextAlertNotificationRowModel>): Intent {
            return Intent(context, AlertRowRemoteViewsService::class.java).apply {
                putParcelableArrayListExtra(
                    ROWS_EXTRA_KEY,
                    ArrayList<NextAlertNotificationRowModel>(rows)
                )
            }
        }
    }

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val rows = intent.getParcelableArrayListExtra(ROWS_EXTRA_KEY)
            ?: ArrayList<NextAlertNotificationRowModel>()
        return AlertRowViewsFactory(applicationContext, rows)
    }
}

class AlertRowViewsFactory(
    private val context: Context,
    private val items: java.util.ArrayList<NextAlertNotificationRowModel>
) : RemoteViewsService.RemoteViewsFactory {

    override fun onCreate() {}

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onDataSetChanged() {}

    override fun hasStableIds(): Boolean = true

    override fun getViewAt(position: Int): RemoteViews {
        return AlertNotificationRowView(context).apply {
            items.getOrNull(position)?.let { model -> bind(model) }
        }
    }

    override fun getCount(): Int = items.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}

    private fun getItem(index: Int): NextAlertNotificationRowModel? {
        return items.getOrNull(index)
    }
}
