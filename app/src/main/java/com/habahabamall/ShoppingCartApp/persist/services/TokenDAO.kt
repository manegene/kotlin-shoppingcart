package com.habahabamall.ShoppingCartApp.persist.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.habahabamall.ShoppingCartApp.persist.models.PersistToken

@Dao
interface TokenDAO {
    @Query("select count (*) from tokens")
    suspend fun ifExists(): Int

    @Query("select * from tokens order by id Desc limit 1")
    suspend fun getAll(): PersistToken

    @Insert
    suspend fun addToken(token: PersistToken)

    @Update
    suspend fun updateToken(newToken: PersistToken)
}