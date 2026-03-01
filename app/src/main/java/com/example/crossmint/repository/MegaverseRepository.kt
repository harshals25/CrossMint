package com.example.crossmint.repository

import com.example.crossmint.network.MegaverseApi
import com.example.crossmint.network.model.ComethRequest
import com.example.crossmint.network.model.PolyanetRequest
import com.example.crossmint.network.model.SoloonRequest

class MegaverseRepository(
    private val api: MegaverseApi
) {
    suspend fun getGoal(candidateId: String): List<List<String>> {
        return api.getGoal(candidateId).goal
    }

    suspend fun putPolyanet(candidateId: String, row: Int, col: Int) {
        api.createPolyanet(
            PolyanetRequest(candidateId = candidateId, row = row, column = col)
        )
    }

    suspend fun deletePolyanet(candidateId: String, row: Int, col: Int) {
        api.deletePolyanet(
            PolyanetRequest(candidateId = candidateId, row = row, column = col)
        )
    }

    suspend fun putSoloons(candidateId: String, row: Int, col: Int, color: String){
        api.createSoloon(
            SoloonRequest(candidateId, row, col, color)
        )
    }

    suspend fun putComeths(candidateId: String, row: Int, col: Int, direction: String) {
        api.createCometh(ComethRequest(candidateId, row, col, direction))
    }


}