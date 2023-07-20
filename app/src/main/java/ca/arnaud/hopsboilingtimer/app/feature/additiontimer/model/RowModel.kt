package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model


data class AdditionRowModel(
    val id: String = "",
    val title: String = "",
    val duration: String = "",
    val options: List<AdditionOptionType> = emptyList()
)

data class AlertRowModel(
    val id: String = "",
    val title: String = "",
    val duration: String = "",
    val disabled: Boolean = false,
    val highlighted: Boolean = false,
    val addChecked: Boolean? = null // only show when not null
)
