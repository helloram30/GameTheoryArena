import { useMemo, useState } from 'react';
import type { MatchResult } from '../types';

interface MatchReplayProps {
  matches: MatchResult[];
}

function winner(match: MatchResult): string {
  if (match.playerOneTotal > match.playerTwoTotal) {
    return match.playerOneStrategy;
  }
  if (match.playerTwoTotal > match.playerOneTotal) {
    return match.playerTwoStrategy;
  }
  return 'Draw';
}

function moveLabel(move: 'COOPERATE' | 'DEFECT'): string {
  return move === 'COOPERATE' ? 'Peace' : 'Hit';
}

export default function MatchReplay({ matches }: MatchReplayProps) {
  const [selectedIndex, setSelectedIndex] = useState(0);

  const selectedMatch = useMemo(() => {
    if (matches.length === 0) {
      return null;
    }
    return matches[Math.min(selectedIndex, matches.length - 1)];
  }, [matches, selectedIndex]);

  if (!selectedMatch) {
    return null;
  }

  return (
    <section className="panel">
      <h2>Match Replay</h2>
      <div className="replayControls">
        <label htmlFor="match-select">Select match</label>
        <select
          id="match-select"
          value={selectedIndex}
          onChange={(event) => setSelectedIndex(Number(event.target.value))}
        >
          {matches.map((match, index) => (
            <option key={`${match.playerOneStrategy}-${match.playerTwoStrategy}`} value={index}>
              {match.playerOneStrategy} vs {match.playerTwoStrategy}
            </option>
          ))}
        </select>
      </div>

      <p className="matchSummary">
        Winner: <strong>{winner(selectedMatch)}</strong> | Score: {selectedMatch.playerOneStrategy} {selectedMatch.playerOneTotal} - {selectedMatch.playerTwoTotal}{' '}
        {selectedMatch.playerTwoStrategy}
      </p>

      <div className="tableWrap replayTable">
        <table>
          <thead>
            <tr>
              <th>Round</th>
              <th>{selectedMatch.playerOneStrategy}</th>
              <th>{selectedMatch.playerTwoStrategy}</th>
              <th>Round Score</th>
            </tr>
          </thead>
          <tbody>
            {selectedMatch.rounds.map((round) => (
              <tr key={round.round}>
                <td>{round.round}</td>
                <td>{moveLabel(round.playerOneMove)}</td>
                <td>{moveLabel(round.playerTwoMove)}</td>
                <td>
                  {round.playerOneScore} - {round.playerTwoScore}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
