package ca.arnaud.hopsboilingtimer.app.model

data class RowModel(
    val id: String = "",
    val title: String = "",
    val duration: String = "",
    val options: List<AdditionOptionType> = emptyList()
)