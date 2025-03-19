package com.teamox.woongstock.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.teamox.woongstock.data.StockTable

@Dao
interface ProductDao {
    @Insert
    fun productRegistration(product: ProductTable)

    @Query("SELECT * FROM product_table WHERE id = :primaryKeyValue")
    fun getProduct(primaryKeyValue: Int): ProductTable

    @Query("SELECT * FROM product_table ORDER BY id DESC")
    fun getAll(): List<ProductTable>

    @Query("UPDATE product_table SET quantity = :newQuantity WHERE id = :productId")
    suspend fun updateQuantity(productId: Int, newQuantity: String)

    @Insert
    suspend fun insertLogistics(data: LogisticsTable)

    @Transaction
    suspend fun updateQuantityAndInsertLogistics(productId: Int, newQuantity: String, date: String, type: String, client: String, memo: String, quantity: String) {
        updateQuantity(productId, newQuantity)
        insertLogistics(LogisticsTable(id = 0, productId = productId, date = date, type = type, client = client, memo = memo, quantity = quantity))
    }

    @Query("SELECT * FROM product_table")
    fun getAllProducts(): LiveData<List<ProductTable>>
}