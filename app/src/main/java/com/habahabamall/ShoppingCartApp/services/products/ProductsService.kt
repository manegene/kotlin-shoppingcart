package com.habahabamall.ShoppingCartApp.services.products

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat.getString
import com.habahabamall.ShoppingCartApp.MainApplication
import com.habahabamall.ShoppingCartApp.R
import com.habahabamall.ShoppingCartApp.models.products.ProductsModel
import com.habahabamall.ShoppingCartApp.services.authentication.ApiService
import javax.inject.Inject

class ProductsService(application: MainApplication, context: Context) : IProductsService {
    private var product = ProductsModel()
    private val authInterceptor = application.authInterceptor
    private var productList = listOf<ProductsModel>()

    @Inject
    var apiService: ApiService = ApiService(application, context)

    private val url:String= getString(context,R.string.api_root)
    private val productsApiService: IProductsService =
        application.app(url).create(IProductsService::class.java)


    //get all products in a list
    override suspend fun getAllProducts(): List<ProductsModel> {
        //first get a new api and assign it to new bearer
        authInterceptor.updateToken(apiService.useStoredToken())

        try {

            productList = productsApiService.getAllProducts()
            // Log.d("better",productList.toString())

        }
        catch (ex: Exception) {
            Log.e("all products error", ex.toString())
        }

        return productList
    }


    //return single product item that has been clicked/selected
    override suspend fun getProductById(id: Int): ProductsModel {
        //get a new access token if less than an hour.
        authInterceptor.updateToken(apiService.useStoredToken())
        try {
            //Log.e("pid ther",id.toString())
            product = productsApiService.getProductById(id)

        }
        catch (ex: Exception) {
            Log.e("single product error", ex.toString())
        }
        return product
    }
}