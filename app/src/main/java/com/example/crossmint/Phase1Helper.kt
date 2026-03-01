package com.example.crossmint

import com.example.crossmint.repository.MegaverseRepository

suspend fun runPhase1(candidateId: String, repo: MegaverseRepository) {

    // get the goal, it gives where we need to place the polyanets
    val goal = repo.getGoal(candidateId)


    // when we see a polyanets call the API
    for (r in goal.indices) {
        for (c in goal[r].indices) {
            if (goal[r][c] == "POLYANET")
                repo.putPolyanet(candidateId, r, c)
        }
    }
}


