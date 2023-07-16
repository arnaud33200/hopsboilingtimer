package ca.arnaud.hopsboilingtimer.app.di

import androidx.lifecycle.ViewModel
import ca.arnaud.hopsboilingtimer.app.MainViewModel
import ca.arnaud.hopsboilingtimer.app.feature.additiontimer.AdditionTimerViewModel
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
    interface MainViewModelFactory : ViewModelAssistedFactory<MainViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(factory: MainViewModelFactory): ViewModelAssistedFactory<out ViewModel>

    @AssistedFactory
    interface AdditionTimerViewModelFactory : ViewModelAssistedFactory<AdditionTimerViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(AdditionTimerViewModel::class)
    abstract fun bindAdditionTimerViewModel(factory: AdditionTimerViewModelFactory): ViewModelAssistedFactory<out ViewModel>
}