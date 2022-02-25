package com.example.saloonservice.RoomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cartlist")
class CartList(
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "groupid") val groupId: String,
    @ColumnInfo(name = "image") val images: String,
    @ColumnInfo(name = "name") val service: String,
    @ColumnInfo(name = "price") val price: String )
