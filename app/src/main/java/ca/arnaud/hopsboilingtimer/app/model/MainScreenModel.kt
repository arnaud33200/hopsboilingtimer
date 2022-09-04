package ca.arnaud.hopsboilingtimer.app.model

data class MainScreenModel(
    val additionRows: List<AdditionRowModel> = emptyList()
)

data class AdditionRowModel(
    val title: String = "",
    val duration: String = ""
)
