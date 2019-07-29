package com.kusu.linkedinlogin.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthTokenInterceptor(private val authToken: String?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if (authToken != null)
            builder.addHeader("Authorization", "Bearer $authToken")
        builder.addHeader("cache-control", "no-cache")
        builder.addHeader("X-Restli-Protocol-Version", "2.0.0")
        return chain.proceed(builder.build())
    }
}
