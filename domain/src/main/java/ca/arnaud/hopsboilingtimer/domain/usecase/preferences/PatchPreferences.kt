package ca.arnaud.hopsboilingtimer.domain.usecase.preferences

import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.model.preferences.PatchPreferencesParams
import ca.arnaud.hopsboilingtimer.domain.model.preferences.Preferences
import ca.arnaud.hopsboilingtimer.domain.repository.preferences.PreferencesRepository
import ca.arnaud.hopsboilingtimer.domain.usecase.common.CoroutineContextProvider
import ca.arnaud.hopsboilingtimer.domain.usecase.common.SuspendableUseCase
import javax.inject.Inject

class PatchPreferences @Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    private val preferencesRepository: PreferencesRepository,
) : SuspendableUseCase<PatchPreferencesParams, Response<Preferences, Throwable>>(
    coroutineContextProvider
) {

    override suspend fun buildRequest(params: PatchPreferencesParams): Response<Preferences, Throwable> {
        return preferencesRepository.patchPreferences(params)
    }
}