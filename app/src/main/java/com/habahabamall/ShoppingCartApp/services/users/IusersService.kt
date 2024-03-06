package com.habahabamall.ShoppingCartApp.services.users

import com.habahabamall.ShoppingCartApp.models.products.ProductsModel
import com.habahabamall.ShoppingCartApp.models.users.GetCartAndWishModel
import com.habahabamall.ShoppingCartApp.models.users.UsersModel
import com.habahabamall.ShoppingCartApp.models.users.UsersResponse
import com.habahabamall.ShoppingCartApp.persist.models.PersistUser
import retrofit2.http.Body
import retrofit2.http.POST

interface IusersService {

    //login a user
    @POST("apiuser/login")
    suspend fun loginUserService(@Body user: UsersModel): UsersResponse

    //check is a user id is locally saved
    suspend fun checkUser(): Boolean

    //get peristed user login id
    suspend fun getLoggedUser(): PersistUser

    //get user cart and wishlist items
     @POST("cart/getcartitem")
     suspend fun getUserCartandWish(@Body wishCart:GetCartAndWishModel):List<ProductsModel>

     //Add user wishlist or cart item
}