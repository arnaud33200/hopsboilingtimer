package ca.arnaud.hopsboilingtimer.app.model

data class MainScreenModel(
    val additionRows: List<RowModel> = emptyList(),
    val newAdditionRow: NewAdditionModel? = NewAdditionModel(),
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

data class NewAdditionModel(
    val title: String = "",
    val duration: String = "",
    val buttonEnabled: Boolean = false
)

enum class AdditionOptionType {
    Delete
}
