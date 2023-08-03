package ca.arnaud.hopsboilingtimer.domain.repository.preferences

import ca.arnaud.hopsboilingtimer.domain.common.Response
import ca.arnaud.hopsboilingtimer.domain.model.preferences.Preferences
import ca.arnaud.hopsboilingtimer.domain.model.preferences.PatchPreferencesParams
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun patchPreferences(params: PatchPreferencesParams): Response<Preferences, Throwable>

    fun getPreferencesFlow(): Flow<Preferences>
}