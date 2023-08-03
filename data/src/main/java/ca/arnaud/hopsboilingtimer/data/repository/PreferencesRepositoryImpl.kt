package ca.arnaud.hopsboilingtimer.data.repository

import ca.arnaud.hopsboilingtimer.data.datasource.PreferencesLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.model.preferences.PatchPreferencesParams
import ca.arnaud.hopsboilingtimer.domain.model.preferences.Preferences
import ca.arnaud.hopsboilingtimer.domain.repository.preferences.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesLocalDataSource: PreferencesLocalDataSource,
) : PreferencesRepository {

    override suspend fun patchPreferences(params: PatchPreferencesParams): Response<Preferences, Throwable> {
        val currentPreferences = getPreferencesFlow().first()
        val newPreferences = currentPreferences.copy(
            darkMode = params.darkMode ?: currentPreferences.darkMode
        )
        preferencesLocalDataSource.setPreferences(newPreferences)
        return Response.ofSuccess(newPreferences)
    }

    override fun getPreferencesFlow(): Flow<Preferences> {
        return preferencesLocalDataSource.getPreferences().map {
            it ?: Preferences()
        }
    }
}