package ca.arnaud.hopsboilingtimer.app.extension

import androidx.compose.material3.Typography
import ca.arnaud.hopsboilingtimer.app.theme.AppTypography

fun AppTypography.toMaterialTypography(): Typography {
    return Typography(
        displayLarge = h1,
        displayMedium = h2,
        displaySmall = h3,
        headlineLarge = h4,
        headlineMedium = h5,
        headlineSmall = h6,
        titleLarge = subtitle1,
        titleMedium = subtitle2,
        titleSmall = body1,
        bodyLarge = body2,
        bodyMedium = button,
        bodySmall = caption,
        labelLarge = overline,
    )
}