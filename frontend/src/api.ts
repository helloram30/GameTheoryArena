import type { TournamentResult } from './types';

const BASE_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api';

export async function fetchStrategies(): Promise<string[]> {
  const response = await fetch(`${BASE_URL}/strategies`);
  if (!response.ok) {
    throw new Error('Failed to load strategies');
  }
  return response.json();
}

export async function runTournament(rounds: number, seed?: number): Promise<TournamentResult> {
  const response = await fetch(`${BASE_URL}/tournament`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ rounds, seed })
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || 'Tournament failed');
  }

  return response.json();
}
