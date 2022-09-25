package ca.arnaud.hopsboilingtimer.app.extension

import androidx.compose.material.Colors
import ca.arnaud.hopsboilingtimer.app.theme.HopsAppColors

fun HopsAppColors.toMaterialColors(): Colors {
    return Colors(
        primary = primary,
        primaryVariant = primaryVariant,
        secondary = secondary,
        secondaryVariant = secondaryVariant,
        background = background,
        surface = surface,
        error = error,
        onPrimary = onPrimary,
        onSecondary = onSecondary,
        onBackground = onBackground,
        onSurface = onSurface,
        onError = onError,
        isLight = isLight,
    )
}