package com.example.submissionexpert1.application.di

import com.example.submissionexpert1.BuildConfig
import com.example.submissionexpert1.data.api.ApiService
import com.example.submissionexpert1.data.api.interceptors.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

  @Provides
  @Singleton
  fun provideOkHttpClient(authInterceptor : AuthInterceptor) : OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor(
        if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        else HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

      )
      .connectTimeout(120, TimeUnit.SECONDS)
      .readTimeout(120, TimeUnit.SECONDS)
      .addInterceptor(authInterceptor)
      .build()
  }

  @Provides
  @Singleton
  fun provideApiService(okHttpClient : OkHttpClient) : ApiService {
    val retrofit = Retrofit.Builder()
      .baseUrl(BuildConfig.API_BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(okHttpClient)
      .build()
    return retrofit.create(ApiService::class.java)
  }
}