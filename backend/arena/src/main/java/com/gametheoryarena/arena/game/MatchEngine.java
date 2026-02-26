package com.gametheoryarena.arena.game;

import java.util.ArrayList;
import java.util.List;

import com.gametheoryarena.arena.model.MatchResult;
import com.gametheoryarena.arena.model.Move;
import com.gametheoryarena.arena.model.RoundResult;
import com.gametheoryarena.arena.strategy.Strategy;

public class MatchEngine {
    private static final int TEMPTATION = 5; // defect while other cooperates
    private static final int REWARD = 3; // mutual cooperation
    private static final int PUNISHMENT = 1; // mutual defection
    private static final int SUCKER = 0; // cooperate while other defects

    public MatchResult play(Strategy playerOne, Strategy playerTwo, int rounds) {
        List<Move> historyOne = new ArrayList<>();
        List<Move> historyTwo = new ArrayList<>();
        List<RoundResult> results = new ArrayList<>();
        int scoreOne = 0;
        int scoreTwo = 0;
        for (int round = 1; round <= rounds; round++) {
            Move moveOne = playerOne.nextMove(historyOne, historyTwo);
            Move moveTwo = playerTwo.nextMove(historyTwo, historyOne);
            int payoffOne = payoff(moveOne, moveTwo);
            int payoffTwo = payoff(moveTwo, moveOne);
            scoreOne += payoffOne;
            scoreTwo += payoffTwo;
            historyOne.add(moveOne);
            historyTwo.add(moveTwo);
            results.add(new RoundResult(round, moveOne, moveTwo, payoffOne, payoffTwo));
        }
        return new MatchResult(playerOne.name(), playerTwo.name(), scoreOne, scoreTwo, results);
    }

    private int payoff(Move self, Move opponent) {
        if (self == Move.COOPERATE && opponent == Move.COOPERATE) {
            return REWARD;
        }
        if (self == Move.DEFECT && opponent == Move.COOPERATE) {
            return TEMPTATION;
        }
        if (self == Move.DEFECT) {
            return PUNISHMENT;
        }
        return SUCKER;
    }
}
