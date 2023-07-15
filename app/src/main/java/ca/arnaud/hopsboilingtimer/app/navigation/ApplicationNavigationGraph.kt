package ca.arnaud.hopsboilingtimer.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import ca.arnaud.hopsboilingtimer.app.di.assistedviewmodel.AssistedViewModelProviderFactory
import ca.arnaud.hopsboilingtimer.app.navigation.ApplicationNavigationGraphConfig.mainNavigationRoute
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.navigation.AdditionTimerNavigation
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.navigation.addAdditionTimerDestination
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.navigation

object ApplicationNavigationGraphConfig {
    const val mainNavigationRoute = "main"
}

@Composable
fun ApplicationNavigationGraph(
    navController: NavHostController,
    viewModelAssistedFactory: AssistedViewModelProviderFactory,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = mainNavigationRoute,
    ) {

        navigation(
            route = mainNavigationRoute,
            startDestination = AdditionTimerNavigation.route,
        ) {

            addAdditionTimerDestination(
                navController = navController,
                viewModelAssistedFactory = viewModelAssistedFactory,
            )
        }
    }
}