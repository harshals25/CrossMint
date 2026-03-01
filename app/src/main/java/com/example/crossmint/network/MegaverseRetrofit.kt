package com.example.crossmint.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MegaverseRetrofit {
    private val BASE_CROSSMINT_URL = "https://challenge.crossmint.io/"


    val api: MegaverseApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_CROSSMINT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MegaverseApi::class.java)
    }
}