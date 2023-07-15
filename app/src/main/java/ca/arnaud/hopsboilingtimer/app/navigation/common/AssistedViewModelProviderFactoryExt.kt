package ca.arnaud.hopsboilingtimer.app.navigation.common

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.savedstate.SavedStateRegistryOwner
import ca.arnaud.hopsboilingtimer.app.di.assistedviewmodel.AssistedViewModelProviderFactory

@Composable
inline fun <reified T : ViewModel> AssistedViewModelProviderFactory.createViewModel(
    args: Bundle? = null,
    viewModelStoreOwner: ViewModelStoreOwner? = null
): T {
    val vmStoreOwner = viewModelStoreOwner ?: checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewmodelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val factory = when (vmStoreOwner) {
        is NavBackStackEntry -> create(vmStoreOwner, args)
        is SavedStateRegistryOwner -> create(vmStoreOwner, args)
        else -> null //This will probably crash because it means the factory isn't coming from dagger
    }

    return viewModel(
        viewModelStoreOwner = vmStoreOwner,
        factory = factory
    )
}