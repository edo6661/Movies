package com.example.submissionexpert1.data.api.interceptors

import com.example.submissionexpert1.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
) : Interceptor {

  override fun intercept(chain : Interceptor.Chain) : Response {
    val originalRequest = chain.request()
    // TODO: replace / add kalo udah nambahin auth tmdb
    val token = BuildConfig.ACCESS_TOKEN
    return if (token.isNotEmpty()) {
      val newRequest = originalRequest.newBuilder()
        .header("Authorization", "Bearer $token")
        .build()
      chain.proceed(newRequest)
    } else {
      chain.proceed(originalRequest)
    }
  }
}
