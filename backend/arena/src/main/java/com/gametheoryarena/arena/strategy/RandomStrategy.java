package com.gametheoryarena.arena.strategy;

import java.util.List;
import java.util.Random;

import com.gametheoryarena.arena.model.Move;

public class RandomStrategy implements Strategy {
    private final Random random;

    public RandomStrategy(Random random) {
        this.random = random;
    }

    @Override
    public String name() {
        return "Random";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        return random.nextBoolean() ? Move.COOPERATE : Move.DEFECT;
    }
}
