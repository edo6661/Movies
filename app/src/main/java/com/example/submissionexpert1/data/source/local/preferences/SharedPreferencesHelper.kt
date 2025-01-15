package com.example.submissionexpert1.data.source.local.preferences

import android.content.Context

class SharedPreferencesHelper(context : Context) {

  private val prefs = context.getSharedPreferences("genre_data_prefs", Context.MODE_PRIVATE)

  fun isDataImported() : Boolean {
    return prefs.getBoolean(KEY_DATA_IMPORTED, false)
  }

  fun setDataImported() {
    prefs.edit().putBoolean(KEY_DATA_IMPORTED, true).apply()
  }

  fun clearDataImportedFlag() {
    prefs.edit().putBoolean(KEY_DATA_IMPORTED, false).apply()
  }

  companion object {

    private const val KEY_DATA_IMPORTED = "data_imported"
  }
}

