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

    @Query("SELECT * FROM logistics_table ORDER BY id DESC")
    fun getLogisticsAll(): List<LogisticsTable>

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

    @Query("SELECT logistics_table.id AS logisticsId, logistics_table.productId, logistics_table.quantity AS increment, logistics_table.date, logistics_table.type, logistics_table.client, logistics_table.memo, product_table.quantity AS productQuantity, product_table.name AS productName, product_table.image AS productImage FROM logistics_table INNER JOIN product_table ON logistics_table.productId = product_table.id ORDER BY logistics_table.date ASC")
    fun getHistoryList(): List<HistoryList>
}
