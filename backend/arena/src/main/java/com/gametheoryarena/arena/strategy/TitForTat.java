package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public class TitForTat implements Strategy {
    @Override
    public String name() {
        return "TitForTat";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        if (opponentHistory.isEmpty()) {
            return Move.COOPERATE;
        }
        return opponentHistory.get(opponentHistory.size() - 1);
    }
}
