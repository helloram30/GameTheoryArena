package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public class AdaptiveThreshold implements Strategy {
    private double threshold = 0.5;

    @Override
    public String name() {
        return "AdaptiveThreshold";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        if (!opponentHistory.isEmpty()) {
            long opponentDefections = opponentHistory.stream().filter(move -> move == Move.DEFECT).count();
            double defectionRate = (double) opponentDefections / opponentHistory.size();
            threshold = 0.6 * threshold + 0.4 * defectionRate;
            if (defectionRate > threshold) {
                return Move.DEFECT;
            }
        }
        return Move.COOPERATE;
    }
}
