package com.habahabamall.ShoppingCartApp.persist.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.habahabamall.ShoppingCartApp.persist.models.PersistProductDetails

@Dao
interface ProductDAO {
    @Query("SELECT COUNT(*) FROM productDetails")
    suspend fun getProductCount(): Int

    @Query("select * from productDetails")
    fun getCurrent(): PersistProductDetails

    @Insert
    suspend fun addProduct(vararg newProd: PersistProductDetails)

    @Update
    suspend fun updateNew(updated: PersistProductDetails)
}