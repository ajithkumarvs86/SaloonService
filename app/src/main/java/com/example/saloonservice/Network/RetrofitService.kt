package com.example.saloonservice.Network

import com.example.saloonservice.Utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private  val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val servicecall : Servicecall by lazy {
        retrofit.create(Servicecall::class.java)
    }

}