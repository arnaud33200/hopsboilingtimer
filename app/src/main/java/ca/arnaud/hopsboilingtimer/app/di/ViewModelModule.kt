package ca.arnaud.hopsboilingtimer.app.di

import androidx.lifecycle.ViewModel
import ca.arnaud.hopsboilingtimer.app.AdditionTimerViewModel
import ca.arnaud.hopsboilingtimer.app.di.assistedviewmodel.ViewModelAssistedFactory
import ca.arnaud.hopsboilingtimer.app.di.assistedviewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.assisted.AssistedFactory
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelModule {

    @AssistedFactory
    interface AdditionTimerViewModelFactory : ViewModelAssistedFactory<AdditionTimerViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(AdditionTimerViewModel::class)
    abstract fun bindAdditionTimerViewModel(factory: AdditionTimerViewModelFactory): ViewModelAssistedFactory<out ViewModel>
}