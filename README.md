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

## Notes

- CORS is enabled for `http://localhost:5173`.
- Anti-cheat, accounts, and persistence are not included in this MVP.
