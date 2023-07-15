package ca.arnaud.hopsboilingtimer.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import ca.arnaud.hopsboilingtimer.app.di.assistedviewmodel.AssistedViewModelProviderFactory
import ca.arnaud.hopsboilingtimer.app.navigation.ApplicationNavigationGraph
import ca.arnaud.hopsboilingtimer.app.theme.HopsAppTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelAssistedFactory: AssistedViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContent {
            val navController = rememberAnimatedNavController()

            // have an actually main view model with just the theme (for now)
//            val viewModel = viewModelAssistedFactory.createViewModel<MainViewModel>(
//                args = savedInstanceState
//            )
//            val darkMode by viewModel.darkMode.collectAsState()

            val darkMode: Boolean? = null
            HopsAppTheme(
                darkTheme = darkMode ?: isSystemInDarkTheme(),
            ) {
                ApplicationNavigationGraph(
                    navController = navController,
                    viewModelAssistedFactory = viewModelAssistedFactory
                )
            }
        }
    }
}
