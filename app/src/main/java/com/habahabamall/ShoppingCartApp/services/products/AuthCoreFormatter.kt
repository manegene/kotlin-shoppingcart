package com.habahabamall.ShoppingCartApp.services.products

import okhttp3.Interceptor
import okhttp3.Response

class AuthCoreFormatter : Interceptor {
    private var authToken: String? = null

    fun updateToken(newToken: String) {
        authToken = newToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // Check if authToken is not null and add Authorization header
        authToken?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}


