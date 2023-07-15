package ca.arnaud.hopsboilingtimer.app.di.assistedviewmodel

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

interface ViewModelAssistedFactory<T : ViewModel> {

    fun create(savedStateHandle: SavedStateHandle, defaultArgs: Bundle?): T
}