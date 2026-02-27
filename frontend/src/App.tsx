import { FormEvent, useEffect, useMemo, useState } from 'react';
import { fetchStrategies, runTournament } from './api';
import Leaderboard from './components/Leaderboard';
import MatchReplay from './components/MatchReplay';
import type { CustomStrategyRequest, FirstMove, ResponseMode, TournamentResult } from './types';

const DEFAULT_ROUNDS = 200;

export default function App() {
  const [strategies, setStrategies] = useState<string[]>([]);
  const [rounds, setRounds] = useState<number>(DEFAULT_ROUNDS);
  const [seed, setSeed] = useState<string>('');
  const [includeCustomStrategy, setIncludeCustomStrategy] = useState(false);
  const [customName, setCustomName] = useState('MyRuleBot');
  const [firstMove, setFirstMove] = useState<FirstMove>('COOPERATE');
  const [responseMode, setResponseMode] = useState<ResponseMode>('MIRROR_LAST');
  const [forgivenessProbability, setForgivenessProbability] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [result, setResult] = useState<TournamentResult | null>(null);

  useEffect(() => {
    fetchStrategies()
      .then(setStrategies)
      .catch((loadError: Error) => setError(loadError.message));
  }, []);

  const totalMatches = useMemo(() => {
    const hasValidCustomName = customName.trim().length > 0;
    const extra = includeCustomStrategy && hasValidCustomName ? 1 : 0;
    const n = strategies.length + extra;
    return (n * (n + 1)) / 2;
  }, [strategies, includeCustomStrategy, customName]);

  const onSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const parsedSeed = seed.trim().length === 0 ? undefined : Number(seed);
      if (parsedSeed !== undefined && Number.isNaN(parsedSeed)) {
        throw new Error('Seed must be a number');
      }

      let customStrategies: CustomStrategyRequest[] | undefined;
      if (includeCustomStrategy) {
        const normalizedName = customName.trim();
        if (normalizedName.length === 0) {
          throw new Error('Custom strategy name cannot be blank');
        }
        if (strategies.includes(normalizedName)) {
          throw new Error(`Custom strategy name conflicts with built-in strategy: ${normalizedName}`);
        }
        customStrategies = [
          {
            name: normalizedName,
            firstMove,
            responseMode,
            forgivenessProbability: Math.max(0, Math.min(1, forgivenessProbability))
          }
        ];
      }

      const tournamentResult = await runTournament(rounds, parsedSeed, customStrategies);
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

        <div className="customBuilder">
          <h3>Custom Strategy Builder (V1)</h3>
          <label className="toggleRow">
            <input
              type="checkbox"
              checked={includeCustomStrategy}
              onChange={(event) => setIncludeCustomStrategy(event.target.checked)}
            />
            Include one custom rule-based strategy in the tournament
          </label>

          {includeCustomStrategy && (
            <div className="builderGrid">
              <div className="controlField">
                <label htmlFor="custom-name">Strategy name</label>
                <input
                  id="custom-name"
                  type="text"
                  value={customName}
                  onChange={(event) => setCustomName(event.target.value)}
                />
              </div>

              <div className="controlField">
                <label htmlFor="custom-first-move">First move</label>
                <select
                  id="custom-first-move"
                  value={firstMove}
                  onChange={(event) => setFirstMove(event.target.value as FirstMove)}
                >
                  <option value="COOPERATE">Peace</option>
                  <option value="DEFECT">Hit</option>
                </select>
              </div>

              <div className="controlField">
                <label htmlFor="custom-response-mode">Response rule</label>
                <select
                  id="custom-response-mode"
                  value={responseMode}
                  onChange={(event) => setResponseMode(event.target.value as ResponseMode)}
                >
                  <option value="MIRROR_LAST">Mirror opponent last move</option>
                  <option value="DEFECT_AFTER_ONE">Defect after one opponent defect</option>
                  <option value="DEFECT_AFTER_TWO">Defect after two consecutive opponent defects</option>
                  <option value="ALWAYS_COOPERATE">Always cooperate after round 1</option>
                  <option value="ALWAYS_DEFECT">Always defect after round 1</option>
                </select>
              </div>

              <div className="controlField">
                <label htmlFor="custom-forgiveness">Forgiveness probability (0 to 1)</label>
                <input
                  id="custom-forgiveness"
                  type="number"
                  min={0}
                  max={1}
                  step={0.05}
                  value={forgivenessProbability}
                  onChange={(event) => setForgivenessProbability(Number(event.target.value))}
                />
              </div>
            </div>
          )}
        </div>

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
