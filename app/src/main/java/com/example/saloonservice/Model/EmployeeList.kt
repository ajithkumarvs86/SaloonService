package com.example.saloonservice.Model

data class EmployeeList(
    val `data`: List<Data>,
    val responseCode: Int
)

data class Data(
    val id: Int,
    val image: String,
    val name: String
)



