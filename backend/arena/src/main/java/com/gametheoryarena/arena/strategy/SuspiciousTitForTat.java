package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public class SuspiciousTitForTat implements Strategy {
    @Override
    public String name() {
        return "SuspiciousTitForTat";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        if (opponentHistory.isEmpty()) {
            return Move.DEFECT;
        }
        return opponentHistory.get(opponentHistory.size() - 1);
    }
}
