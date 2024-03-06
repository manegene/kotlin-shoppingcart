package com.habahabamall.ShoppingCartApp.persist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.habahabamall.ShoppingCartApp.persist.models.PersistProductDetails
import com.habahabamall.ShoppingCartApp.persist.models.PersistToken
import com.habahabamall.ShoppingCartApp.persist.models.PersistUser
import com.habahabamall.ShoppingCartApp.persist.services.ProductDAO
import com.habahabamall.ShoppingCartApp.persist.services.TokenDAO
import com.habahabamall.ShoppingCartApp.persist.services.UserDAO


@Database(
    entities = [PersistProductDetails::class, PersistToken::class, PersistUser::class],
    version = 12, exportSchema = false
)
abstract class DAOAppDatabase : RoomDatabase() {
    abstract fun getToken(): TokenDAO
    abstract fun getProduct(): ProductDAO

    abstract fun getUser(): UserDAO

    companion object {
        @Volatile
        private var Instance: DAOAppDatabase? = null

        private val migration11to12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Write SQL statements to migrate the database schema from version 10 to version 11
                // Create a new table with the desired schema

                // Drop the old table
                db.execSQL("DROP TABLE user")

                db.execSQL(
                    "CREATE TABLE user (id INTEGER PRIMARY KEY  NOT NULL," +
                            " email TEXT NOT NULL,firstName TEXT NOT NULL," +
                            "lastName TEXT NOT NULL,addressId INTEGER NOT NULL)"
                )

            }
        }

        fun getDatabase(context: Context): DAOAppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, DAOAppDatabase::class.java, "item_database")
                    .addMigrations(migration11to12)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }

    override fun clearAllTables() {
        // Override clearAllTables method to prevent Room from throwing an exception
        // when attempting to clear tables on version change
    }
}