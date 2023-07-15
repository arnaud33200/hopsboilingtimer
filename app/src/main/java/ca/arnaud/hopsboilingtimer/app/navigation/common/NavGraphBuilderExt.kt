package ca.arnaud.hopsboilingtimer.app.navigation.common

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import ca.arnaud.hopsboilingtimer.app.di.assistedviewmodel.AssistedViewModelProviderFactory
import com.google.accompanist.navigation.animation.composable

typealias AnimatedTransition<Transition> = AnimatedContentScope<NavBackStackEntry>.() -> Transition?

inline fun <reified VM : ViewModel> NavGraphBuilder.navigationComposable(
    screenNavigation: ScreenNavigation,
    viewModelAssistedFactory: AssistedViewModelProviderFactory,
    animatedTransitions: AnimatedTransitions = DefaultAnimatedTransitions,
    crossinline content: @Composable AnimatedVisibilityScope.(NavBackStackEntry, VM) -> Unit
) {
    // TODO - setup deeplinks and arguments here if needed
    composable(
        route = screenNavigation.route,
        enterTransition = animatedTransitions.enterTransition,
        exitTransition = animatedTransitions.exitTransition,
        popEnterTransition = animatedTransitions.popEnterTransition,
        popExitTransition = animatedTransitions.popExitTransition,
        content = { backStackEntry ->
            val viewModel = viewModelAssistedFactory.createViewModel<VM>(
                args = backStackEntry.arguments
            )
            content(backStackEntry, viewModel)
        }
    )
}

interface AnimatedTransitions {
    val enterTransition: AnimatedTransition<EnterTransition>?
    val exitTransition: AnimatedTransition<ExitTransition>?
    val popEnterTransition: AnimatedTransition<EnterTransition>?
    val popExitTransition: AnimatedTransition<ExitTransition>?
}

object DefaultAnimatedTransitions : AnimatedTransitions {
    override val enterTransition: AnimatedTransition<EnterTransition> = {
        slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween())
    }
    override val exitTransition: AnimatedTransition<ExitTransition>? = null
    override val popEnterTransition: AnimatedTransition<EnterTransition>? = null
    override val popExitTransition: AnimatedTransition<ExitTransition> = {
        slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween())
    }
}