package com.example.crossmint.network.model

data class SoloonRequest(
    val candidateId: String,
    val row: Int,
    val column: Int,
    val color: String // blue red purple white
)
