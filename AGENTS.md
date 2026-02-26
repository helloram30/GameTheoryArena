# AGENTS.md

## Project Direction
- Build this as a web app.
- Backend stack: Java (Spring Boot).
- Frontend stack: React + TypeScript.
- Core gameplay uses automated bot/AI strategies (not manual human turns).

## Arena Rules
- Strategies play repeated rounds against each other.
- Actions are effectively `Hit` (defect) or `Peace` (cooperate).
- Choices are revealed after both agents make each round decision.
- Winner logic is score-based over `N` rounds.

## Tournament Settings
- Tournament format: round robin.
- Ranking metric: win rate.
- Keep default payoff model: Iterated Prisoner's Dilemma (`T=5, R=3, P=1, S=0`).
- Default rounds per match: 200 unless overridden.

## Scope Priorities
- Primary goal is ranking strategies/theories based on arena wars.
- Anti-cheat and related hardening are deferred to later iterations.

## Collaboration Preference
- Ask before pushing code to git remotes.
