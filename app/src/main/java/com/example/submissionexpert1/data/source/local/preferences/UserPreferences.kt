package com.example.submissionexpert1.data.source.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.model.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences @Inject constructor(
  private val context : Context,
) {

  private val gson = Gson()
  private val dataStore = context.dataStore

  suspend fun saveUserSession(user : User) = dataStore.edit {
    it[USER_DATA_KEY] = gson.toJson(user)
  }

  fun getUserData() : Flow<User?> = dataStore.data.map { preferences ->
    preferences[USER_DATA_KEY]?.let {
      gson.fromJson(it, User::class.java)
    }
  }

  suspend fun clearUserSession() = dataStore.edit {
    it.clear()
  }


  companion object {

    private val USER_DATA_KEY = stringPreferencesKey("user_data")
  }
}