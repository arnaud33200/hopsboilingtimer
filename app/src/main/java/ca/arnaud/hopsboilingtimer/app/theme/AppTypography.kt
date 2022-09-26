package ca.arnaud.hopsboilingtimer.app.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

sealed interface AppTypography {
    val h1: TextStyle
    val h2: TextStyle
    val h3: TextStyle
    val h4: TextStyle
    val h5: TextStyle
    val h6: TextStyle

    val subtitle1: TextStyle
    val subtitle2: TextStyle

    val body1: TextStyle
    val body2: TextStyle

    val button: TextStyle
    val caption: TextStyle
    val overline: TextStyle
}

object DefaultAppTypography: AppTypography {
    override val h1 = TextStyle.Default
    override val h2 = TextStyle.Default
    override val h3 = TextStyle.Default
    override val h4 = TextStyle.Default
    override val h5 = TextStyle.Default
    override val h6 = TextStyle.Default

    override val subtitle1 = TextStyle.Default
    override val subtitle2 = TextStyle.Default

    override val body1 = TextStyle.Default
    override val body2 = TextStyle.Default

    override val button = TextStyle.Default
    override val caption = TextStyle.Default
    override val overline = TextStyle.Default
}

object HopsAppTypography: AppTypography {
    override val h1 = createTextStyle(fontSize = 17.sp)
    override val h2 = createTextStyle(fontSize = 15.sp)
    override val h3 = createTextStyle(fontSize = 14.sp)
    override val h4 = createTextStyle(fontSize = 13.sp) // probably won't use
    override val h5 = createTextStyle(fontSize = 12.sp) // Won't use lol
    override val h6 = createTextStyle(fontSize = 12.sp) // Won't use lol

    override val subtitle1 = createTextStyle(fontSize = 11.sp)
    override val subtitle2 = createTextStyle(fontSize = 10.sp)

    override val body1 = createTextStyle(fontSize = 12.sp)
    override val body2 = createTextStyle(fontSize = 12.sp)

    override val button = createTextStyle(fontSize = 14.sp)
    override val caption = createTextStyle(fontSize = 12.sp)
    override val overline = createTextStyle(fontSize = 12.sp)
}

private fun createTextStyle(
    fontSize: TextUnit,
    fontFamily: FontFamily = FontFamily.SansSerif,
    fontWeight: FontWeight = FontWeight.Normal

) = TextStyle(
    fontSize = fontSize,
    fontFamily = fontFamily,
    fontWeight = fontWeight
)