package ca.arnaud.hopsboilingtimer.app

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import ca.arnaud.hopsboilingtimer.app.screen.MainScreen
import ca.arnaud.hopsboilingtimer.app.theme.HopsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContent {
            val darkMode = viewModel.darkMode.collectAsState().value
            HopsAppTheme(
                darkTheme = darkMode ?: isSystemInDarkTheme(),
            ) {
                MainScreen(viewModel)
            }
        }
    }
}
