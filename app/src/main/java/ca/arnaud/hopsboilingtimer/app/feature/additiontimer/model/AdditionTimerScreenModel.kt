package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.model

import ca.arnaud.hopsboilingtimer.app.feature.common.model.TimeButtonModel

sealed interface AdditionTimerScreenModel {
    data class Edit(
        val additionRows: List<AdditionRowModel> = emptyList(),
        val newAdditionRow: NewAdditionModel = NewAdditionModel(),
        val bottomBarModel: BottomBarModel = BottomBarModel()
    ) : AdditionTimerScreenModel

    data class Schedule(
        val additionRows: List<AlertRowModel> = emptyList(),
        val bottomBarModel: BottomBarModel = BottomBarModel()
    ) : AdditionTimerScreenModel
}


data class BottomBarModel(
    val timeButton: TimeButtonModel = TimeButtonModel(),
)

data class NewAdditionModel(
    val title: String = "",
    val duration: String = "",
    val buttonEnabled: Boolean = false
)

enum class AdditionOptionType {
    Delete
}
