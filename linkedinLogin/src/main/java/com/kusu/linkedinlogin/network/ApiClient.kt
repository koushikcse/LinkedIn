package com.kusu.linkedinlogin.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        fun getApiClient(authToken: String): Retrofit {
            return Retrofit.Builder()
                .client(
                    OkHttpClient.Builder()
                        .connectTimeout(90, TimeUnit.SECONDS)
                        .readTimeout(90, TimeUnit.SECONDS)
                        .writeTimeout(90, TimeUnit.SECONDS)
                        .addInterceptor(AuthTokenInterceptor(authToken))
                        .build()
                )
                .baseUrl("https://api.linkedin.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}