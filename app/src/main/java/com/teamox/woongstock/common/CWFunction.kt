package com.teamox.woongstock.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.teamox.woongstock.activity.AppDatabase

object CWFunction {
    fun getDatabase(context: Context): AppDatabase {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "stock-database"
        ).build()

        return db
    }

    fun setIntSharedPref(context: Context, prefName: String, key: String, value: Int){
        val sharedPref = context.getSharedPreferences(prefName, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(key, value)

        editor.commit()
    }

    fun getIntSharedPref(context: Context, prefName: String, key: String): Int{
        val sharedPref = context.getSharedPreferences(prefName, MODE_PRIVATE)
        val value = sharedPref.getInt(key, 0)

        return value
    }

}