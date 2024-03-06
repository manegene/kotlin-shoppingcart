package com.habahabamall.ShoppingCartApp

import android.app.Application
import androidx.work.Configuration
import com.google.firebase.BuildConfig
import com.habahabamall.ShoppingCartApp.services.products.AuthCoreFormatter
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {

    // Logging level
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(
                if (BuildConfig.DEBUG) android.util.Log.DEBUG
                else android.util.Log.ERROR
            )
            .build()


    fun auth(url:String): Retrofit {
        //firebase auth
        return Retrofit.Builder()
            .baseUrl("https://$url")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    //products api
    val authInterceptor = AuthCoreFormatter()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()


    fun app(url: String): Retrofit {
        // Initialize product Retrofit
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://$url")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



}

