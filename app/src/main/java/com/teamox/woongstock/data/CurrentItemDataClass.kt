package com.teamox.woongstock.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class CurrentItem(
    val id: String,
    val image: String,
    val name: String,
    val price: String,
    val quantity: String)

@Entity(tableName = "stock_table")
data class StockTable(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val image: String,
    @ColumnInfo val price: String,
    @ColumnInfo val quantity: String
)