package ca.arnaud.hopsboilingtimer.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import ca.arnaud.hopsboilingtimer.app.extension.toMaterialColors
import ca.arnaud.hopsboilingtimer.app.extension.toMaterialTypography

@OptIn(ExperimentalMaterialApi::class) // TODO - put in gradle
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
            colors = LocalAppColors.current.toMaterialColors(),
            typography = LocalAppTypography.current.toMaterialTypography(),
            shapes = Shapes,
            content = content
        )
    }
}

val LocalAppColors = staticCompositionLocalOf<HopsAppColors> { DefaultHopsAppColors() }
val LocalAppTypography = staticCompositionLocalOf<AppTypography> { DefaultAppTypography }




