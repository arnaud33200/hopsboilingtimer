package ca.arnaud.hopsboilingtimer.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import ca.arnaud.hopsboilingtimer.data.datasource.PreferencesLocalDataSource
import ca.arnaud.hopsboilingtimer.domain.model.preferences.Preferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.datastore.preferences.core.Preferences as AndroidPreferences

class PreferencesLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<AndroidPreferences>,
) : PreferencesLocalDataSource {

    companion object {
        const val DARK_THEME_PREFERENCES_KEY = "dark_theme"
    }

    private val darkThemePreferenceKey = booleanPreferencesKey(DARK_THEME_PREFERENCES_KEY)

    // TODO - that will be a pain to add each param individually, let's find a way to store a PreferencesLocalData instead

    override fun getPreferences(): Flow<Preferences?> {
        return dataStore.data
            .map { androidPreferences ->
            Preferences(
                darkMode = androidPreferences[darkThemePreferenceKey]
            )
        }
    }

    override suspend fun setPreferences(preferences: Preferences) {
        dataStore.edit { mutablePreferences ->
            preferences.darkMode?.let { mutablePreferences[darkThemePreferenceKey] = it }
        }
    }
}