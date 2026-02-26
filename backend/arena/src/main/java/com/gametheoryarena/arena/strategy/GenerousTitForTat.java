package com.gametheoryarena.arena.strategy;

import java.util.List;
import java.util.Random;

import com.gametheoryarena.arena.model.Move;

public class GenerousTitForTat implements Strategy {
    private final double forgivenessProbability;
    private final Random random;

    public GenerousTitForTat(Random random, double forgivenessProbability) {
        this.random = random;
        this.forgivenessProbability = forgivenessProbability;
    }

    @Override
    public String name() {
        return "GenerousTitForTat";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        if (opponentHistory.isEmpty()) {
            return Move.COOPERATE;
        }
        Move lastOpponent = opponentHistory.get(opponentHistory.size() - 1);
        if (lastOpponent == Move.DEFECT && random.nextDouble() > forgivenessProbability) {
            return Move.DEFECT;
        }
        return Move.COOPERATE;
    }
}
