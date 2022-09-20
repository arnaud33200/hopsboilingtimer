package ca.arnaud.hopsboilingtimer.app.model

data class MainScreenModel(
    val additionRows: List<AdditionRowModel> = emptyList(),
    val newAdditionRow: AdditionRowModel? = AdditionRowModel(),
    val bottomBarModel: BottomBarModel = BottomBarModel()
)

data class BottomBarModel(
    // TODO probably better to have a button model
    val buttonTitle: String = "",
    val buttonTime: String = "",
    val buttonStyle: ButtonStyle = ButtonStyle.Start,
)

enum class ButtonStyle {
    Start, Stop
}

data class AdditionRowModel(
    val id: String = "",
    val title: String = "",
    val duration: String = ""
)
