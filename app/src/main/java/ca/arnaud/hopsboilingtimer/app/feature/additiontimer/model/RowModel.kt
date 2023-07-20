package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model


data class AdditionRowModel(
    val id: String = "",
    val title: String = "",
    val duration: String = "",
    val options: List<AdditionOptionType> = emptyList()
)

sealed interface AlertRowModel {

    val id: String
    data class Next(
        override val id: String = "",
        val title: String = "",
        val time: String = "",
        val highlighted: Boolean = false,
    ) : AlertRowModel

    data class Added(
        override val id: String = "",
        val title: String = "",
        val time: String = "",
        val checked: Boolean
    ) : AlertRowModel
}
