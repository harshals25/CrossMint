package com.example.crossmint

import android.util.Log
import com.example.crossmint.repository.MegaverseRepository
import kotlinx.coroutines.delay
import retrofit2.HttpException

suspend fun runPhase2(candidateId: String, repository: MegaverseRepository) {
    // get the goal similar to phase 1
    val goal = repository.getGoal(candidateId)

    //Place polyanets
    // we do this first because soloons must be adjacent to Polyanets
    for (r in goal.indices) {
        for (c in goal[r].indices) {
            if (goal[r][c] == "POLYANET") {
                // this is required because the endpoint returned 429 a bunch of times,
                // likely reason is due to a rate limit.
                // more info about retryOn429 below.
                retryOn429 {
                    repository.putPolyanet(candidateId, r, c)
                }
                Log.d("Megaverse", "Placed polyanet at ($r,$c)")
                delay(300)
            }
        }
    }

    // 2) Place soloons
    // condition of placing soloons next to polynets is needed when we dont trust the goal given
    // challenge mentions use goal API so placing soloons after polyanets.
    // if API validation was not present and had to be done here, we'd have to check neighbors and see if polyanets exists
    // and then only place the soloons
    // no specific directions: 1) Don't place the soloon 2) Return error
    // goal response should already satisfy this but enforcing it defensively
    for (r in goal.indices) {
        for (c in goal[r].indices) {
            val cell = goal[r][c]
            if (cell.endsWith("_SOLOON")) {
                val color = cell.substringBefore("_").lowercase()
                // check neighbors, if they are polyanets
                if (hasAdjacentPolyanet(goal, r, c)) {
                    retryOn429 {
                        repository.putSoloons(candidateId, r, c, color)
                    }
                    Log.d("Megaverse", "Placed Soloons at ($r,$c) with color $color")
                    delay(300)
                }
            }
        }
    }

    // 3) Place comeths
    // these can be alone but always have a direction
    // again trusting the goal API response
    // a check for if cometh has a direction can be placed before calling the putCometh API
    // no specific directions: 1) Don't place the cometh 2) Return error
    // goal response should already satisfy this but enforcing it defensively
    for (r in goal.indices) {
        for (c in goal[r].indices) {
            val cell = goal[r][c]
            // check if the cometh has a valid direction
            if (cell.endsWith("_COMETH") && hasValidDirection(cell)) {
                val direction = cell.substringBefore("_").lowercase()
                retryOn429 {
                    repository.putComeths(candidateId, r, c, direction)
                }
                Log.d("Megaverse", "Placed cometh at ($r,$c) with direction $direction")
                delay(300)
            }
        }
    }

    Log.d("Megaverse", "Phase 2 done")
}

/**
* Retries a network request when we see 429 from server so that we can resume building our map instead of starting again
* There must be a rate limit on how frequently requests can be made resulting in 429 for very quick requests
* This method leverages exponential backoff, each attempt adds a cool down period
* Attempt 1-> 1 second, Attempt 2 -> 2 seconds, Attempt 3 -> 4 seconds (capped at 20 seconds)
* But only HTTP 429 are retried, if there is other error we don't retry
*/

suspend fun <T> retryOn429(
    maxRetries: Int = 6, // cap so we can break out of the retries
    initialDelayMs: Long = 1000, // setting the initial delay
    factor: Double = 2.0, // doubling it after every attempt
    block: suspend () -> T
): T {
    var delayMs = initialDelayMs
    var attempt = 0

    while (true) {
        try {
            return block()
        } catch (e: HttpException) {
            val code = e.code()
            if (code != 429) throw e

            attempt++
            if (attempt > maxRetries) {
                Log.d("Megaverse", "429 again. Giving up after $maxRetries retries.")
                throw e
            }

            Log.d(
                "Megaverse",
                "Got 429. Backing off for ${delayMs}ms (attempt $attempt/$maxRetries)"
            )
            delay(delayMs)
            // never letting delay get more than 20 seconds
            delayMs = (delayMs * factor).toLong().coerceAtMost(20_000)
        }
    }
}

fun hasAdjacentPolyanet(goal: List<List<String>>, r: Int, c: Int): Boolean {

    val directions = listOf(
        Pair(-1, 0),
        Pair(1, 0),
        Pair(0, -1),
        Pair(0, 1)
    )

    for ((dr, dc) in directions) {
        val nr = r + dr
        val nc = c + dc

        if (nr in goal.indices && nc in goal[nr].indices) {
            if (goal[nr][nc] == "POLYANET") {
                return true
            }
        }
    }

    return false
}

fun hasValidDirection(cell: String): Boolean {

    val validDirections = setOf(
        "UP",
        "DOWN",
        "LEFT",
        "RIGHT"
    )

    val parts = cell.split("_")

    if (parts.size != 2) return false

    val direction = parts[0]

    return direction in validDirections
}