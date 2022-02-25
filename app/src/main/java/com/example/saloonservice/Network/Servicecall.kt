package com.example.saloonservice.Network

import com.example.saloonservice.Model.*
import retrofit2.Response
import retrofit2.http.GET

interface Servicecall {

    // To Get List Of Services
    @GET("/challenge-services/")
    suspend fun getServiceList(): Response<ServiceList>

    // To Get List of Employees
    @GET("/challenge-employees/")
    suspend fun getEmployeeList(): Response<EmployeeList>

}