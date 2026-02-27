import type { Standing } from '../types';
import StrategyName from './StrategyName';

interface LeaderboardProps {
  standings: Standing[];
}

function pct(value: number): string {
  return `${(value * 100).toFixed(1)}%`;
}

function fixed(value: number): string {
  return value.toFixed(3);
}

export default function Leaderboard({ standings }: LeaderboardProps) {
  return (
    <section className="panel">
      <h2>Population Payoff Leaderboard</h2>
      <div className="tableWrap">
        <table>
          <thead>
            <tr>
              <th>Rank</th>
              <th>Strategy</th>
              <th>Avg/Round</th>
              <th>Win Rate</th>
              <th>W</th>
              <th>D</th>
              <th>L</th>
              <th>Points</th>
              <th>Diff</th>
            </tr>
          </thead>
          <tbody>
            {standings.map((standing, index) => (
              <tr key={standing.strategy}>
                <td>{index + 1}</td>
                <td>
                  <StrategyName name={standing.strategy} />
                </td>
                <td>{fixed(standing.averageScorePerRound)}</td>
                <td>{pct(standing.winRate)}</td>
                <td>{standing.wins}</td>
                <td>{standing.draws}</td>
                <td>{standing.losses}</td>
                <td>{fixed(standing.totalPoints)}</td>
                <td>{standing.pointDifferential}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
