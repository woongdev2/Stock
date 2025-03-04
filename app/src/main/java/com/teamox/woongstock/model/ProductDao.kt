package com.teamox.woongstock.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.teamox.woongstock.data.StockTable

@Dao
interface ProductDao {
    @Insert
    fun productRegistration(product: ProductTable)

    @Query("SELECT * FROM product_table WHERE id = :primaryKeyValue")
    fun getProduct(primaryKeyValue: Int): ProductTable

    @Query("SELECT * FROM product_table ORDER BY id DESC")
    fun getAll(): List<ProductTable>
}