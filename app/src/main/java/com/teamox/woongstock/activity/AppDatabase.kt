package com.teamox.woongstock.activity

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teamox.woongstock.model.DAO
import com.teamox.woongstock.data.StockTable
import com.teamox.woongstock.model.LogisticsTable
import com.teamox.woongstock.model.ProductDao
import com.teamox.woongstock.model.ProductTable

@Database(entities = [StockTable::class, ProductTable::class, LogisticsTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockDao(): DAO
    abstract fun productDao(): ProductDao
}

