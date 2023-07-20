package ca.arnaud.hopsboilingtimer.app.feature.common.model

data class TimeButtonModel(
    val title: String = "",
    val time: String = "",
    val enabled: Boolean = true,
    val style: TimeButtonStyle = TimeButtonStyle.Start,
)

enum class TimeButtonStyle {
    Start, Stop
}