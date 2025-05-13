package com.teamox.woongstock.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_table")
data class ProductTable(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val image: String,
    @ColumnInfo val location: String,
    @ColumnInfo val category: String,
    @ColumnInfo val marginRate: String,
    @ColumnInfo val stockDate: String,
    @ColumnInfo val shippingDate: String,
    @ColumnInfo val purchasePrice: String,
    @ColumnInfo val sellingPrice: String,
    @ColumnInfo val quantity: String,
    @ColumnInfo val memo: String
)

@Entity(tableName = "logistics_table")
data class LogisticsTable(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val productId: Int,
    val date: String,
    val type: String,
    val client: String,
    val memo: String,
    val existingQuantity: String,
    val increment: String
)

data class HistoryList(
    val logisticsId: Int,
    val productId: Int,
    val increment: String,
    val date: String,
    val type: String,
    val client: String,
    val memo: String,
    val existingQuantity: String,
    val productName: String,
    val productImage: String
)

data class ProductIncrement(
    val productId: Int,
    val totalIncrement: Int,
    val productName: String,
    val productImage: String
)