package ca.arnaud.hopsboilingtimer.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import ca.arnaud.hopsboilingtimer.app.extension.toMaterialColors

@OptIn(ExperimentalMaterialApi::class) // TODO - put in gradle
@Composable
fun HopsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        localAppColors provides DarkHopsAppColors(),
        LocalMinimumTouchTargetEnforcement provides false // remove padding around checkbox
    ) {
        MaterialTheme(
            colors = localAppColors.current.toMaterialColors(),
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

val localAppColors = staticCompositionLocalOf<HopsAppColors> { DefaultHopsAppColors() }

object HopsAppTheme {
    val colors: HopsAppColors @Composable get() = localAppColors.current
}



