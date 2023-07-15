package ca.arnaud.hopsboilingtimer.app.di.assistedviewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import javax.inject.Inject

typealias AssistedFactoryMap = Map<Class<out ViewModel>, @JvmSuppressWildcards ViewModelAssistedFactory<out ViewModel>>

class AssistedViewModelProviderFactory @Inject constructor(
    private val factories: AssistedFactoryMap
) {

    fun create(
        savedStateRegistryOwner: SavedStateRegistryOwner,
        defaultArgs: Bundle?
    ): AbstractSavedStateViewModelFactory {
        return object : AbstractSavedStateViewModelFactory(savedStateRegistryOwner, defaultArgs) {

            @Suppress("UNCHECKED_CAST")
            override fun <VM : ViewModel> create(
                key: String,
                modelClass: Class<VM>,
                handle: SavedStateHandle,
            ): VM {
                return factories[modelClass]?.let { viewModelAssistedFactory ->
                    viewModelAssistedFactory.create(handle, defaultArgs) as VM
                } ?: throw IllegalStateException(
                    "Failed to get ViewModel from provider map for model $modelClass"
                )
            }
        }
    }
}

