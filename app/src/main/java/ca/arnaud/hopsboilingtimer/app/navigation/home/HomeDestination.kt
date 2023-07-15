package ca.arnaud.hopsboilingtimer.app.navigation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ca.arnaud.hopsboilingtimer.app.MainViewModel
import ca.arnaud.hopsboilingtimer.app.di.assistedviewmodel.AssistedViewModelProviderFactory
import ca.arnaud.hopsboilingtimer.app.navigation.common.createViewModel
import ca.arnaud.hopsboilingtimer.app.navigation.common.navigationComposable
import ca.arnaud.hopsboilingtimer.app.screen.MainScreen

fun NavGraphBuilder.addHomeDestination(
    navController: NavHostController,
    viewModelAssistedFactory: AssistedViewModelProviderFactory,
) {
    navigationComposable<MainViewModel>(
        screenNavigation = HomeNavigation,
        viewModelAssistedFactory = viewModelAssistedFactory,
    ) { backStackEntry, viewModel ->
        HomeDestination(viewModel)
    }
}

@Composable
private fun HomeDestination(viewModel: MainViewModel) {
    val model by viewModel.screenModel.collectAsState()
    val showRequestPermissionDialog by viewModel.showRequestPermissionDialog.collectAsState()

    MainScreen(
        model = model,
        showRequestPermissionDialog = showRequestPermissionDialog,
        actionListener = viewModel,
    )
}