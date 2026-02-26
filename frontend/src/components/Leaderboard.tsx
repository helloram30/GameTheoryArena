import type { Standing } from '../types';

interface LeaderboardProps {
  standings: Standing[];
}

function pct(value: number): string {
  return `${(value * 100).toFixed(1)}%`;
}

export default function Leaderboard({ standings }: LeaderboardProps) {
  return (
    <section className="panel">
      <h2>Win Rate Leaderboard</h2>
      <div className="tableWrap">
        <table>
          <thead>
            <tr>
              <th>Rank</th>
              <th>Strategy</th>
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
                <td>{standing.strategy}</td>
                <td>{pct(standing.winRate)}</td>
                <td>{standing.wins}</td>
                <td>{standing.draws}</td>
                <td>{standing.losses}</td>
                <td>{standing.totalPoints}</td>
                <td>{standing.pointDifferential}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
