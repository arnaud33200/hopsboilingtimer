package ca.arnaud.hopsboilingtimer.app.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
sealed interface HopsAppColors {

    val primary: Color
    val primaryVariant: Color
    val onPrimary: Color

    val secondary: Color
    val secondaryVariant: Color
    val onSecondary: Color

    val background: Color
    val onBackground: Color

    val surface: Color
    val onSurface: Color

    val error: Color
    val onError: Color

    val isLight: Boolean
}

data class DefaultHopsAppColors(
    override val primary: Color = Color.Unspecified,
    override val primaryVariant: Color = Color.Unspecified,
    override val onPrimary: Color = Color.Unspecified,

    override val secondary: Color = Color.Unspecified,
    override val secondaryVariant: Color = Color.Unspecified,
    override val onSecondary: Color = Color.Unspecified,

    override val background: Color = Color.Unspecified,
    override val onBackground: Color = Color.Unspecified,

    override val surface: Color = Color.Unspecified,
    override val onSurface: Color = Color.Unspecified,

    override val error: Color = Color.Unspecified,
    override val onError: Color = Color.Unspecified,

    override val isLight: Boolean = false

): HopsAppColors

data class DarkHopsAppColors(
    override val primary: Color = Color(0xFF36A35C),
    override val primaryVariant: Color = Color(0xFF30854D),
    override val onPrimary: Color = Color(0xFFFFFFFF),

    override val secondary: Color = Color(0xFF3171EE),
    override val secondaryVariant: Color = Color(0xFF1A53E6),
    override val onSecondary: Color = Color(0xFFFFFFFF),

    override val background: Color = Color(0xFF222222),
    override val onBackground: Color = Color(0xFFFFFFFF),

    override val surface: Color = Color(0xFF202020),
    override val onSurface: Color = Color(0xFFFFFFFF),

    override val error: Color = Color(0xFFEC4646),
    override val onError: Color = Color(0xFFFFFFFF),

    override val isLight: Boolean = false

): HopsAppColors