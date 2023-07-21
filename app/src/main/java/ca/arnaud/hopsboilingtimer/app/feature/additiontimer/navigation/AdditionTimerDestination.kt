package ca.arnaud.hopsboilingtimer.app.feature.additiontimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ca.arnaud.hopsboilingtimer.app.di.assistedviewmodel.AssistedViewModelProviderFactory
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.AdditionTimerViewModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.screen.AdditionTimerScreen
import ca.arnaud.hopsboilingtimer.app.navigation.common.navigationComposable
import ca.arnaud.hopsboilingtimer.app.view.NotificationPermissionDialog

fun NavGraphBuilder.addAdditionTimerDestination(
    navController: NavHostController,
    viewModelAssistedFactory: AssistedViewModelProviderFactory,
) {
    navigationComposable<AdditionTimerViewModel>(
        screenNavigation = AdditionTimerNavigation,
        viewModelAssistedFactory = viewModelAssistedFactory,
    ) { backStackEntry, viewModel ->
        AdditionTimerDestination(viewModel)
    }
}

@Composable
private fun AdditionTimerDestination(viewModel: AdditionTimerViewModel) {
    val model by viewModel.screenModel.collectAsState()
    val timerTextUpdate by viewModel.timerTextUpdate.collectAsState()
    val newAddition by viewModel.newAddition.collectAsState()
    val showRequestPermissionDialog by viewModel.showRequestPermissionDialog.collectAsState()

    AdditionTimerScreen(
        model = model,
        timerTextUpdate = { timerTextUpdate },
        newAddition = { newAddition },
        actionListener = viewModel,
    )

    if (showRequestPermissionDialog) {
        NotificationPermissionDialog(onPermissionResult = viewModel::onPermissionResult)
    }
}