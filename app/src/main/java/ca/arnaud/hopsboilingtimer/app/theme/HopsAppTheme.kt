package ca.arnaud.hopsboilingtimer.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumTouchTargetEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import ca.arnaud.hopsboilingtimer.app.extension.toMaterialColorScheme
import ca.arnaud.hopsboilingtimer.app.extension.toMaterialTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HopsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAppColors provides when (darkTheme) {
            true -> DarkHopsAppColors()
            false -> LightHopsAppColors()
        },
        LocalAppTypography provides HopsAppTypography,
        LocalMinimumTouchTargetEnforcement provides false // remove padding around checkbox
    ) {
        MaterialTheme(
            colorScheme = LocalAppColors.current.toMaterialColorScheme(),
            typography = LocalAppTypography.current.toMaterialTypography(),
            shapes = Shapes,
            content = content
        )
    }
}

val LocalAppColors = staticCompositionLocalOf<HopsAppColors> { DefaultHopsAppColors() }
val LocalAppTypography = staticCompositionLocalOf<AppTypography> { DefaultAppTypography }




