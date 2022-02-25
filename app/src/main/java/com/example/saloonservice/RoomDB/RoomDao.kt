package com.example.saloonservice.RoomDB

import androidx.room.*


@Dao
interface  RoomDao {

    // To add services to cart
    @Insert()
    fun addCart(User: CartList)

    // To get services added to cart
    @Query(value = "SELECT * FROM cartlist ORDER BY id DESC ")
    fun getCartList() : List<CartList>

}