package com.habahabamall.ShoppingCartApp.persist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tokens")
data class PersistToken(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "tokenId") val token: String = "",
    @ColumnInfo(name = "refreshId") val tokenRefresh: String = "",
    @ColumnInfo("validity") val validTill: String = "",
)