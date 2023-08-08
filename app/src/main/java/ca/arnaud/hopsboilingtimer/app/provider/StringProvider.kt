package ca.arnaud.hopsboilingtimer.app.provider

import androidx.annotation.StringRes

interface StringProvider {

    fun get(@StringRes id: Int): String

    fun get(@StringRes id: Int, vararg arguments: String): String
}