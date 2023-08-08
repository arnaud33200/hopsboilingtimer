package ca.arnaud.hopsboilingtimer.app.fake

import ca.arnaud.hopsboilingtimer.app.extension.expandTemplate
import ca.arnaud.hopsboilingtimer.app.provider.StringProvider

class FakeStringProvider constructor(
    private val stringValueMap: Map<Int, String>
) : StringProvider {

    override fun get(id: Int): String {
        return stringValueMap[id] ?: ""
    }

    override fun get(id: Int, vararg arguments: String): String {
        return get(id).expandTemplate(*arguments)
    }
}