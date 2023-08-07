package ca.arnaud.hopsboilingtimer.app.provider

import android.content.Context
import android.text.TextUtils
import androidx.annotation.StringRes
import javax.inject.Inject

class StringProvider @Inject constructor(
    private val context: Context,
) {

    fun get(@StringRes id: Int): String {
        return context.getString(id)
    }

    fun get(@StringRes id: Int, vararg arguments: String): String {
        return TextUtils.expandTemplate(get(id), *arguments).toString()
    }
}