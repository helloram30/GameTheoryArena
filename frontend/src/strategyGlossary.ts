const strategyGlossary: Record<string, string> = {
  AlwaysCooperate: 'Always chooses Peace. Strong with friendly opponents, easy to exploit by persistent Hit players.',
  AlwaysDefect: 'Always chooses Hit. Exploits cooperators early but usually gets stuck in low-payoff mutual conflict.',
  Random: 'Chooses Peace or Hit randomly each round. Unpredictable, but usually unstable and not payoff-efficient.',
  TitForTat: 'Starts with Peace, then copies the opponent last move. Nice, retaliatory, and quick to restore cooperation.',
  GrimTrigger: 'Starts with Peace. If the opponent hits once, defects forever after. Very harsh and unforgiving.',
  WinStayLoseShift: 'Repeats its last move after a good outcome; switches after a bad one. Adaptive and often cooperative.',
  TitForTwoTats: 'Only hits after two consecutive opponent hits. More forgiving than TitForTat.',
  GenerousTitForTat: 'TitForTat with occasional forgiveness after being hit. Helps recover from noisy conflicts.',
  Prober: 'Uses early probes to test opponents, then adapts based on how exploitable they are.',
  SuspiciousTitForTat: 'TitForTat variant that opens with Hit, then mirrors after that. More defensive by default.',
  ContriteTitForTat: 'TitForTat variant designed to de-escalate accidental conflict and return to cooperation.',
  AdaptiveThreshold: 'Tracks opponent hit rate and switches behavior when defection pressure crosses a threshold.',
  RegretMinimizer: 'Adjusts behavior from observed regret, favoring the action that would have improved past outcomes.'
};

export function strategyExplanation(name: string): string {
  return strategyGlossary[name] ?? 'Custom strategy. Open the backend strategy class for exact decision rules.';
}
