package ca.arnaud.hopsboilingtimer.app.provider

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class StringProvider @Inject constructor(
    private val context: Context,
) {

    fun get(@StringRes id: Int): String {
        return context.getString(id)
    }

    fun get(@StringRes id: Int, vararg arguments: String): String {
        return context.getString(id, arguments)
    }
}