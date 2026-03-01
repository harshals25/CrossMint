package com.example.crossmint.network.model

data class ComethRequest(
    val candidateId: String,
    val row: Int,
    val column: Int,
    val direction: String // up down right left
)