# Crossmint Megaverse Challenge

This project implements the Crossmint Megaverse challange using Kotlin and Retrofit.

The goal is to build Megaverse maps by calling the Crossmint API and placing objects such as Polyanets, Soloons and Comeths at the correct coordinates.

The solution uses the goal endpoint as the source of truth and programmatically builds the map.

---

## Approach

The goal map is retrieved from:

GET /api/map/{candidateId}/goal

The response contains a 2D grid describing what needs to be placed at each coordinate.

Instead of hardcoding coordinates, the program reads the goal map and places objects where needed.

This makes the solution flexible and reusable.

---

## Phase 1

Phase 1 builds a megaverse with only Polyanets.

Steps:

1. Fetch the goal map
2. Iterate through the grid
3. Place a Polyanet wherever `"POLYANET"` appears

Example logic:
```
if (goal[r][c] == "POLYANET") {
repository.putPolyanet(candidateId, r, c)
}
```


---

## Phase 2

Phase 2 builds the Crossmint logo.

Objects are placed in this order:

1. Polyanets  
2. Soloons  
3. Comeths

Polyanets are placed first because Soloons must be adjacent to Polyanets.

The goal response is parsed and the correct API endpoint is called for each object.

---

## Defensive Validation

The goal endpoint should already return a valid map, but some validation is done before making API calls.

### Soloon adjacency

Soloons must be adjacent to a Polyanet.

Before placing a Soloon we verify that at least one neighboring cell contains a Polyanet.

---

### Cometh direction

Comeths must include a valid direction.

Valid directions:

- UP
- DOWN
- LEFT
- RIGHT

Before placing a Cometh we verify that the direction is valid.

---

## Rate Limit Handling

While building the map the API occasionally returned HTTP 429 (Too Many Requests).

To handle this, requests are retried using exponential backoff.

Retry pattern:

- 1 second
- 2 seconds
- 4 seconds
- 8 seconds

The delay is capped at 20 seconds.

Only HTTP 429 responses are retried. Other errors are not retried.

This allows the map to be built reliably without restarting the program.

---

## Project Structure
MegaverseApi.kt

MegaverseRepository.kt

Phase1Helper.kt

Phase2Helper.kt

MainActivity.kt


---

## Running the Project
The project runs when MainActivity is launched.
A coroutine is started using lifecyclecScope to call the APIs

Set your candidateId inside MainActivity and run:

runPhase1(candidateId, repository) for phase 1 

runPhase2(candidateId, repository) for phase 2. 


Only enable one phase at a time.
