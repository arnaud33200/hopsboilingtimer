package ca.arnaud.hopsboilingtimer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import ca.arnaud.hopsboilingtimer.app.screen.MainScreen
import ca.arnaud.hopsboilingtimer.app.theme.HopsBoilingTimerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HopsBoilingTimerTheme {
                MainScreen()
            }
        }
    }
}
