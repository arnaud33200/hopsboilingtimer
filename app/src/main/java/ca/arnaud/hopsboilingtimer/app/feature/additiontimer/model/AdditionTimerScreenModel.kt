package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model

sealed interface AdditionTimerScreenModel {
    data class Edit(
        val additionRows: List<RowModel> = emptyList(),
        val newAdditionRow: NewAdditionModel? = NewAdditionModel(),
        val bottomBarModel: BottomBarModel = BottomBarModel()
    ) : AdditionTimerScreenModel

    data class Schedule(
        val bottomBarModel: BottomBarModel = BottomBarModel()
    ) : AdditionTimerScreenModel
}


data class BottomBarModel(
    // TODO probably better to have a button model
    val buttonTitle: String = "",
    val buttonTime: String = "",
    val buttonEnable: Boolean = false,
    val subButtonTitle: String? = null,
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
