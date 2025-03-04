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
    @ColumnInfo val quantity: String
)