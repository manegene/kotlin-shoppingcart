package com.habahabamall.ShoppingCartApp.services.products

import com.habahabamall.ShoppingCartApp.models.products.ProductsModel
import retrofit2.http.GET
import retrofit2.http.Path

interface IProductsService {

    @GET("products")
    suspend fun getAllProducts(): List<ProductsModel>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductsModel


}