package com.gametheoryarena.arena.strategy;

import java.util.List;
import java.util.Random;

import com.gametheoryarena.arena.model.Move;

public class RegretMinimizer implements Strategy {
    private double regretCooperate = 0.0;
    private double regretDefect = 0.0;
    private final Random random;

    public RegretMinimizer(Random random) {
        this.random = random;
    }

    @Override
    public String name() {
        return "RegretMinimizer";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        if (!selfHistory.isEmpty()) {
            Move lastSelf = selfHistory.get(selfHistory.size() - 1);
            Move lastOpponent = opponentHistory.get(opponentHistory.size() - 1);
            int actual = payoff(lastSelf, lastOpponent);
            int cooperatePayoff = payoff(Move.COOPERATE, lastOpponent);
            int defectPayoff = payoff(Move.DEFECT, lastOpponent);
            if (lastSelf == Move.COOPERATE) {
                regretDefect += Math.max(0, defectPayoff - actual);
            } else {
                regretCooperate += Math.max(0, cooperatePayoff - actual);
            }
        }
        double positiveCooperate = Math.max(0, regretCooperate);
        double positiveDefect = Math.max(0, regretDefect);
        double total = positiveCooperate + positiveDefect;
        if (total == 0) {
            return random.nextBoolean() ? Move.COOPERATE : Move.DEFECT;
        }
        double roll = random.nextDouble() * total;
        return roll < positiveCooperate ? Move.COOPERATE : Move.DEFECT;
    }

    private int payoff(Move self, Move opponent) {
        if (self == Move.COOPERATE && opponent == Move.COOPERATE) {
            return 3;
        }
        if (self == Move.DEFECT && opponent == Move.COOPERATE) {
            return 5;
        }
        if (self == Move.COOPERATE) {
            return 0;
        }
        return 1;
    }
}
