# GameTheoryArena

A web app where game-theory bot strategies battle in a round-robin arena.

- Backend: Java 21, Spring Boot
- Frontend: React + TypeScript + Vite
- Ranking: win rate (`wins + 0.5 * draws`) / matches played
- Match model: Iterated Prisoner's Dilemma (`T=5, R=3, P=1, S=0`)

## Project Structure

- `backend/arena`: Spring Boot API and strategy engine
- `frontend`: React UI for running tournaments and replaying matches

## Run Locally

## 1. Start Backend

```bash
cd backend/arena
./mvnw spring-boot:run
```

Backend runs at `http://localhost:8080`.

## 2. Start Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at `http://localhost:5173`.

## Run With Docker

```bash
docker compose up --build
```

Then open `http://localhost:5173`.

- Frontend container serves the React app through Nginx.
- `/api/*` requests are proxied to the backend container.
- Backend is also exposed directly on `http://localhost:8080`.

## API Endpoints

- `GET /api/strategies`
  - List available strategy bot names.

- `POST /api/match`
  - Run a single match between two strategies.
  - Body:

```json
{
  "playerOneStrategy": "TitForTat",
  "playerTwoStrategy": "AlwaysDefect",
  "rounds": 200,
  "seed": 42
}
```

- `POST /api/tournament`
  - Run round-robin tournament across strategies and return standings + all match replays.
  - If `strategies` is missing/empty, all registered strategies are used.
  - Body:

```json
{
  "strategies": ["TitForTat", "AlwaysDefect", "AlwaysCooperate"],
  "rounds": 200,
  "seed": 42
}
```

- `GET /api/payoff`
  - Returns default payoff description.

## Frontend Features

- Run round-robin tournaments with configurable rounds and optional seed
- View leaderboard ranked by win rate
- Replay each match round-by-round with move reveal (`Peace`/`Hit`)

## Strategy Glossary

- `AlwaysCooperate`: always chooses Peace (`COOPERATE`).
- `AlwaysDefect`: always chooses Hit (`DEFECT`).
- `Random`: randomly chooses Peace or Hit each round.
- `TitForTat`: starts with Peace, then copies opponent's previous move.
- `GrimTrigger`: starts with Peace, but if opponent ever hits once, it hits forever.
- `WinStayLoseShift`: repeats previous move after good outcome, switches after bad outcome.
- `TitForTwoTats`: defects only after opponent defects in two consecutive rounds.
- `GenerousTitForTat`: like TitForTat, but sometimes forgives a defection.
- `Prober`: tests opponent with early defections, then adapts behavior from the response.
- `SuspiciousTitForTat`: TitForTat variant that starts with Hit first.
- `ContriteTitForTat`: TitForTat variant that tries to recover cooperation after accidental conflict.
- `AdaptiveThreshold`: adapts by switching behavior based on opponent defection level.
- `RegretMinimizer`: adapts over time to reduce long-run regret from prior choices.

## Rounds vs Seed

- `rounds` (example: `200`) means how many turns are played in each match.
- `seed` is a random-number initializer for reproducibility.
- Same `seed` + same strategies + same rounds => same stochastic outcomes (useful for repeatable experiments).
- If `seed` is omitted, randomness changes per run.

## Add a New Theory (Strategy)

To add a new theory, create a new strategy class and register it.

1. Create a new class in `backend/arena/src/main/java/com/gametheoryarena/arena/strategy`.
2. Implement the `Strategy` interface:
   - `name()` should return the display name.
   - `nextMove(myHistory, opponentHistory)` should return `Move.COOPERATE` or `Move.DEFECT`.
3. Register it in `StrategyRegistry` by adding a factory entry in the constructor map.
4. Restart backend (or rebuild Docker images) and call `GET /api/strategies` to confirm it appears.

Minimal example:

```java
package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public class PavlovLite implements Strategy {
    @Override
    public String name() {
        return "PavlovLite";
    }

    @Override
    public Move nextMove(List<Move> myHistory, List<Move> opponentHistory) {
        if (myHistory.isEmpty()) {
            return Move.COOPERATE;
        }
        Move myLast = myHistory.get(myHistory.size() - 1);
        Move opponentLast = opponentHistory.get(opponentHistory.size() - 1);
        return myLast == opponentLast ? Move.COOPERATE : Move.DEFECT;
    }
}
```

## Notes

- CORS is enabled for `http://localhost:5173`.
- Anti-cheat, accounts, and persistence are not included in this MVP.
