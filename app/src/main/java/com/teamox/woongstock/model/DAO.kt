package com.teamox.woongstock.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.teamox.woongstock.data.StockTable

@Dao
interface DAO {
    @Query("SELECT * FROM stock_table")
    fun getAll(): List<StockTable>

    @Query("SELECT * FROM stock_table WHERE id = :primaryKeyValue")
    fun getDataWithPrimaryKey(primaryKeyValue: Int): StockTable

    @Insert
    fun insertAll(vararg users: StockTable)
}