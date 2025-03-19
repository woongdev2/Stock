package com.teamox.woongstock.common

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.teamox.woongstock.activity.AppDatabase
import com.teamox.woongstock.model.ProductDao

class DatabaseRepository(private val context: Context) {
    fun getDatabase(): AppDatabase {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "stock-database"
        ).build()

        return db
    }

    fun getPrimaryKey(): Int {
        val sharedPref = context.getSharedPreferences("productPref", Context.MODE_PRIVATE)
        val value = sharedPref.getInt("productId", 0)

        return value
    }

    fun setPrimaryKey(value: Int) {
        val sharedPref = context.getSharedPreferences("productPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt("productId", value)

        editor.commit()
    }

    suspend fun updateQuantityAndLog(productId: Int, newQuantity: String, date: String, type: String, client: String, memo: String, quantity: String) {
        getDatabase().productDao().updateQuantityAndInsertLogistics(productId, newQuantity, date, type, client, memo, quantity)
    }

}