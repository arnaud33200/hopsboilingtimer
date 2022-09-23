package ca.arnaud.hopsboilingtimer.app.model

sealed interface RowModel {

    data class AdditionRowModel(
        val id: String = "",
        val title: String = "",
        val duration: String = "",
        val options: List<AdditionOptionType> = emptyList()
    ) : RowModel
}