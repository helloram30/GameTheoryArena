export type Move = 'COOPERATE' | 'DEFECT';

export interface RoundResult {
  round: number;
  playerOneMove: Move;
  playerTwoMove: Move;
  playerOneScore: number;
  playerTwoScore: number;
}

export interface MatchResult {
  playerOneStrategy: string;
  playerTwoStrategy: string;
  playerOneTotal: number;
  playerTwoTotal: number;
  rounds: RoundResult[];
}

export interface Standing {
  strategy: string;
  matchesPlayed: number;
  wins: number;
  draws: number;
  losses: number;
  winRate: number;
  totalPoints: number;
  averageScorePerRound: number;
  pointDifferential: number;
}

export interface TournamentResult {
  roundsPerMatch: number;
  standings: Standing[];
  matches: MatchResult[];
}

export type FirstMove = 'COOPERATE' | 'DEFECT';
export type ResponseMode =
  | 'MIRROR_LAST'
  | 'DEFECT_AFTER_ONE'
  | 'DEFECT_AFTER_TWO'
  | 'ALWAYS_COOPERATE'
  | 'ALWAYS_DEFECT';

export interface CustomStrategyRequest {
  name: string;
  firstMove: FirstMove;
  responseMode: ResponseMode;
  forgivenessProbability: number;
}
