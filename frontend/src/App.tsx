import { FormEvent, useEffect, useMemo, useState } from 'react';
import { fetchStrategies, runTournament } from './api';
import Leaderboard from './components/Leaderboard';
import MatchReplay from './components/MatchReplay';
import type { TournamentResult } from './types';

const DEFAULT_ROUNDS = 200;

export default function App() {
  const [strategies, setStrategies] = useState<string[]>([]);
  const [rounds, setRounds] = useState<number>(DEFAULT_ROUNDS);
  const [seed, setSeed] = useState<string>('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<TournamentResult | null>(null);

  useEffect(() => {
    fetchStrategies()
      .then(setStrategies)
      .catch((loadError: Error) => setError(loadError.message));
  }, []);

  const totalMatches = useMemo(() => {
    const n = strategies.length;
    return (n * (n + 1)) / 2;
  }, [strategies]);

  const onSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const parsedSeed = seed.trim().length === 0 ? undefined : Number(seed);
      if (parsedSeed !== undefined && Number.isNaN(parsedSeed)) {
        throw new Error('Seed must be a number');
      }
      const tournamentResult = await runTournament(rounds, parsedSeed);
      setResult(tournamentResult);
    } catch (submitError) {
      const message = submitError instanceof Error ? submitError.message : 'Unexpected error';
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app">
      <header>
        <p className="eyebrow">Game Theory Arena</p>
        <h1>War of Strategies</h1>
        <p>
          Run a population-style round-robin tournament with self-play included. Moves are simultaneous each round and
          revealed right after both bots decide.
        </p>
      </header>

      <section className="panel">
        <h2>Run Tournament</h2>
        <p className="missionBlurb">
          Goal: compare strategies in an Iterated Prisoner&apos;s Dilemma population and rank them by long-run payoff
          per round. Higher Avg/Round means a strategy is better at sustaining valuable interactions across the field.
        </p>
        <form onSubmit={onSubmit} className="controlGrid">
          <div className="controlField">
            <label htmlFor="rounds">Rounds per match</label>
            <input
              id="rounds"
              type="number"
              min={1}
              value={rounds}
              onChange={(event) => setRounds(Number(event.target.value))}
            />
          </div>

          <div className="controlField">
            <label htmlFor="seed">Seed (optional)</label>
            <input
              id="seed"
              type="text"
              placeholder="e.g. 42"
              value={seed}
              onChange={(event) => setSeed(event.target.value)}
            />
          </div>

          <div className="controlActions">
            <button type="submit" disabled={loading || strategies.length < 2}>
              {loading ? 'Running...' : 'Run Arena'}
            </button>
          </div>
        </form>

        <p className="meta">Strategies loaded: {strategies.length} | Scheduled matches: {totalMatches}</p>
      </section>

      {error && <p className="error">{error}</p>}

      {result && (
        <>
          <Leaderboard standings={result.standings} />
          <MatchReplay matches={result.matches} />
        </>
      )}
    </div>
  );
}
