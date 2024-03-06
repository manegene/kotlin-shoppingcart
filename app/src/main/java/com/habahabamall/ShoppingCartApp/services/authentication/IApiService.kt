@file:Suppress("SpellCheckingInspection")

package com.habahabamall.ShoppingCartApp.services.authentication

import android.content.Context
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getString
import com.habahabamall.ShoppingCartApp.R
import com.habahabamall.ShoppingCartApp.models.authentication.GetFireBaseToken
import com.habahabamall.ShoppingCartApp.models.authentication.OrderFireBaseToken
import retrofit2.http.Body
import retrofit2.http.POST

@Suppress("SpellCheckingInspection")
interface IApiService {

    // Provide a context to access resources
    fun setContext():String

    //to handle the token ordering and retrieve the returned token
    @POST("v1/accounts:signInWithPassword?key={replace_this}")
    suspend fun tokenService(@Body orderToken: OrderFireBaseToken): GetFireBaseToken

    suspend fun checkStoredToken(): Boolean
    suspend fun useStoredToken(): String

}