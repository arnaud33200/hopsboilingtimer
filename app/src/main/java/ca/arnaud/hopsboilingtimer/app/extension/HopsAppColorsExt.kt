package ca.arnaud.hopsboilingtimer.app.extension

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import ca.arnaud.hopsboilingtimer.app.theme.HopsAppColors

@Composable
fun HopsAppColors.toMaterialColorScheme(): ColorScheme {
    return ColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        error = error,
        onError = onError,

        primaryContainer = MaterialTheme.colorScheme.primaryContainer,
        onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer,
        inversePrimary = MaterialTheme.colorScheme.inversePrimary,
        secondaryContainer = MaterialTheme.colorScheme.secondaryContainer,
        onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer,
        tertiary = MaterialTheme.colorScheme.tertiary,
        onTertiary = MaterialTheme.colorScheme.onTertiary,
        tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer,
        onTertiaryContainer = MaterialTheme.colorScheme.onTertiaryContainer,
        surfaceVariant = MaterialTheme.colorScheme.surfaceVariant,
        onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant,
        surfaceTint = MaterialTheme.colorScheme.surfaceTint,
        inverseSurface = MaterialTheme.colorScheme.inverseSurface,
        inverseOnSurface = MaterialTheme.colorScheme.inverseOnSurface,
        errorContainer = MaterialTheme.colorScheme.errorContainer,
        onErrorContainer = MaterialTheme.colorScheme.onErrorContainer,
        outline = MaterialTheme.colorScheme.outline,
        outlineVariant = MaterialTheme.colorScheme.outlineVariant,
        scrim = MaterialTheme.colorScheme.scrim,
    )
}