package com.example.submissionexpert1.data.source.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.themeDataStore : DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

class ThemePreferences @Inject constructor(
  private val context : Context
) {

  private val dataStore = context.themeDataStore

  fun getThemeMode() : Flow<String> = dataStore.data.map {
    it[THEME_MODE_KEY] ?: THEME_SYSTEM
  }

  suspend fun setThemeMode(themeMode : String) {
    dataStore.edit {
      it[THEME_MODE_KEY] = themeMode
    }
  }

  companion object {

    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"
    const val THEME_SYSTEM = "system"


    private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")

  }
}

