package ca.arnaud.hopsboilingtimer.app.provider

import android.content.Context
import androidx.annotation.StringRes
import ca.arnaud.hopsboilingtimer.app.extension.expandTemplate
import javax.inject.Inject

class StringProviderImpl @Inject constructor(
    private val context: Context,
) : StringProvider {

    override fun get(@StringRes id: Int): String {
        return context.getString(id)
    }

    override fun get(@StringRes id: Int, vararg arguments: String): String {
        return get(id).expandTemplate(*arguments)
    }
}