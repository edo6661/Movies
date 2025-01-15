package com.example.submissionexpert1.application.di

import android.content.Context
import com.example.submissionexpert1.data.source.local.preferences.ThemePreferences
import com.example.submissionexpert1.data.source.local.preferences.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferencesModule {

  @Provides
  @Singleton
  fun provideUserPreferences(@ApplicationContext context : Context) : UserPreferences {
    return UserPreferences(context)
  }

  @Provides
  @Singleton
  fun provideThemePreferences(@ApplicationContext context : Context) : ThemePreferences {
    return ThemePreferences(context)
  }


}