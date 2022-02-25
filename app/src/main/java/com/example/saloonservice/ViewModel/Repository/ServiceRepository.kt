package com.example.saloonservice.ViewModel.Repository

import com.example.saloonservice.Model.EmployeeList
import com.example.saloonservice.Model.ServiceList
import com.example.saloonservice.Network.RetrofitService
import com.example.saloonservice.RoomDB.CartList
import com.example.saloonservice.RoomDB.RoomDao
import retrofit2.Response

class ServiceRepository(private val dao: RoomDao?) {

    // To Get List Of Services
    suspend fun getServiceList() : Response<ServiceList> {
        return RetrofitService.servicecall.getServiceList()
    }

    // To Get List of Employees
    suspend fun getEmployeeList() : Response<EmployeeList> {
        return RetrofitService.servicecall.getEmployeeList()
    }

    // Add service details to cart
    fun addCart(service: CartList){
        dao?.addCart(service)
    }

    // Get service details added to cart
    fun getCartList() : List<CartList>{
        return dao!!.getCartList()
    }
}