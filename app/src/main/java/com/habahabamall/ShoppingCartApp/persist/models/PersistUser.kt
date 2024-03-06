package com.habahabamall.ShoppingCartApp.persist.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class PersistUser(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val addressId: Int = 0

)
