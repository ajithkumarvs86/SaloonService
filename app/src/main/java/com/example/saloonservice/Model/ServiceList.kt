package com.example.saloonservice.Model

data class ServiceList(
    val `data`: List<ServiceData>,
    val responseCode: Int
)

data class ServiceData(
    val id: Int,
    val image: String,
    val name: String,
    val price: Int
)
