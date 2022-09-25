package ca.arnaud.hopsboilingtimer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import ca.arnaud.hopsboilingtimer.app.screen.MainScreen
import ca.arnaud.hopsboilingtimer.app.theme.HopsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
