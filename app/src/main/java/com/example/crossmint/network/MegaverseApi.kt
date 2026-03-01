package com.example.crossmint.network

import com.example.crossmint.network.model.ComethRequest
import com.example.crossmint.network.model.Goal
import com.example.crossmint.network.model.PolyanetRequest
import com.example.crossmint.network.model.SoloonRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface MegaverseApi {
    @GET("api/map/{candidateId}/goal")
    suspend fun getGoal(@Path("candidateId") candidateId: String): Goal

    // Delete requests are not present as they were not used
    // adding just for polyanets to show usage if require

    // Polyanets
    @POST("api/polyanets")
    suspend fun createPolyanet(@Body body: PolyanetRequest)

    @DELETE("api/polyanets")
    suspend fun deletePolyanet(@Body body: PolyanetRequest)

    // Soloons
    @POST("api/soloons")
    suspend fun createSoloon(@Body body: SoloonRequest)

    // Comeths
    @POST("api/comeths")
    suspend fun createCometh(@Body body: ComethRequest)


}