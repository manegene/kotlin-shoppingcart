package com.habahabamall.ShoppingCartApp.persist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productDetails")
data class PersistProductDetails(
    @PrimaryKey val pId: Int = 0,
    @ColumnInfo(name = "ProductId") val storedProductId: Int = 0
)