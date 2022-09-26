package ca.arnaud.hopsboilingtimer.app.extension

import androidx.compose.material.Typography
import ca.arnaud.hopsboilingtimer.app.theme.AppTypography

fun AppTypography.toMaterialTypography(): Typography {
    return Typography(
        h1 = h1,
        h2 = h2,
        h3 = h3,
        h4 = h4,
        h5 = h5,
        h6 = h6,
        subtitle1 = subtitle1,
        subtitle2 = subtitle2,
        body1 = body1,
        body2 = body2,
        button = button,
        caption = caption,
        overline = overline,
    )
}