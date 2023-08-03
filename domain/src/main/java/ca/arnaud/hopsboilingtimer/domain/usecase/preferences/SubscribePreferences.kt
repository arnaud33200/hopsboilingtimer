package ca.arnaud.hopsboilingtimer.domain.usecase.preferences

import ca.arnaud.hopsboilingtimer.domain.model.preferences.Preferences
import ca.arnaud.hopsboilingtimer.domain.repository.preferences.PreferencesRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.NoParamsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribePreferences @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
) : NoParamsUseCase<Flow<Preferences>> {

    override fun execute(): Flow<Preferences> {
        return preferencesRepository.getPreferencesFlow()
    }
}