package ca.arnaud.hopsboilingtimer.app.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ca.arnaud.hopsboilingtimer.app.model.MainScreenModel
import ca.arnaud.hopsboilingtimer.app.theme.HopsBoilingTimerTheme
import kotlinx.coroutines.flow.StateFlow

interface MainScreenViewModel {
    val screenModel: StateFlow<MainScreenModel>
}

@Composable
fun MainScreen() {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Greeting("Android")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HopsBoilingTimerTheme {
        Greeting("Android")
    }
}
