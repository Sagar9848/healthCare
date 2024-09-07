package com.example.healthcarelab.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
//    private const val BASE_URL = "http://ec2-16-16-244-185.eu-north-1.compute.amazonaws.com:3000/"
    private const val BASE_URL = "http://ec2-16-16-244-185.eu-north-1.compute.amazonaws.com:8888/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
