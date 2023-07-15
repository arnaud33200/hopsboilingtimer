package ca.arnaud.hopsboilingtimer.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ca.arnaud.hopsboilingtimer.app.di.assistedviewmodel.AssistedViewModelProviderFactory
import ca.arnaud.hopsboilingtimer.app.navigation.common.createViewModel
import ca.arnaud.hopsboilingtimer.app.screen.MainScreen
import ca.arnaud.hopsboilingtimer.app.theme.HopsAppTheme
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
            val viewModel = viewModelAssistedFactory.createViewModel<MainViewModel>(
                args = savedInstanceState
            )

            val darkMode by viewModel.darkMode.collectAsState()
            HopsAppTheme(
                darkTheme = darkMode ?: isSystemInDarkTheme(),
            ) {
                // TODO - setup navigation

                val model by viewModel.screenModel.collectAsState()
                val showRequestPermissionDialog by viewModel.showRequestPermissionDialog.collectAsState()

                MainScreen(
                    model = model,
                    showRequestPermissionDialog = showRequestPermissionDialog,
                    actionListener = viewModel,
                )
            }
        }
    }
}
