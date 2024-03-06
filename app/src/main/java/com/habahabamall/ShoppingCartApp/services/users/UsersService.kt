package com.habahabamall.ShoppingCartApp.services.users

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getString
import com.habahabamall.ShoppingCartApp.MainApplication
import com.habahabamall.ShoppingCartApp.R
import com.habahabamall.ShoppingCartApp.models.products.ProductsModel
import com.habahabamall.ShoppingCartApp.models.users.GetCartAndWishModel
import com.habahabamall.ShoppingCartApp.models.users.UsersModel
import com.habahabamall.ShoppingCartApp.models.users.UsersResponse
import com.habahabamall.ShoppingCartApp.persist.DAOAppDatabase
import com.habahabamall.ShoppingCartApp.persist.models.PersistUser
import com.habahabamall.ShoppingCartApp.services.authentication.ApiService

class UsersService(application: MainApplication, context: Context) : IusersService {
    private var userResponse = UsersResponse()
    private var products= listOf<ProductsModel>()
    private val url:String=getString(context, R.string.api_root)
    private val userService: IusersService =
        application.app(url).create(IusersService::class.java)
    private val apiService: ApiService = ApiService(application, context)
    private val authInterceptor = application.authInterceptor
    private val dbService = DAOAppDatabase.getDatabase(context)

    override suspend fun loginUserService(user: UsersModel): UsersResponse {
        //first get a new api and assign it to new bearer
        authInterceptor.updateToken(apiService.useStoredToken())
        try {
            userResponse = userService.loginUserService(user)

        }
        catch (exception: Exception) {
            Log.e("login error", exception.toString())
        }
        return userResponse
    }

    override suspend fun checkUser(): Boolean {
        val useIsSaved = dbService.getUser().checkUserExists()
        return useIsSaved > 0

    }

    override suspend fun getLoggedUser(): PersistUser {
        return dbService.getUser().getUser()
    }

    override suspend fun getUserCartandWish(wishCart: GetCartAndWishModel): List<ProductsModel> {
        authInterceptor.updateToken(apiService.useStoredToken())
        try {
            products=userService.getUserCartandWish(wishCart)

        }
        catch (e:Exception){
            //Toast.makeText(this,e,Toast.LENGTH_SHORT).show()
            Log.e("user cart error",e.toString())

        }
        return products
    }
    }