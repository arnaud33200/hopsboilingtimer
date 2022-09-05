package ca.arnaud.hopsboilingtimer.app.model

data class MainScreenModel(
    val additionRows: List<AdditionRowModel> = emptyList(),
    val newAdditionRow: AdditionRowModel = AdditionRowModel()
)

data class AdditionRowModel(
    val title: String = "",
    val duration: String = ""
)
