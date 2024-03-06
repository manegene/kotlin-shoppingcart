package com.habahabamall.ShoppingCartApp.persist.services

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.habahabamall.ShoppingCartApp.persist.models.PersistUser

@Dao
interface UserDAO {
    @Query("select count(*) from user")
    fun checkUserExists(): Int

    @Insert
    fun addNew(user: PersistUser)

    @Query("select * from user")
    fun getUser(): PersistUser

    @Update
    fun updateUser(user: PersistUser)

    @Delete
    fun clearUser(user: PersistUser)
}