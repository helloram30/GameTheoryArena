package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public class GrimTrigger implements Strategy {
    private boolean triggered = false;

    @Override
    public String name() {
        return "GrimTrigger";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        if (!triggered && opponentHistory.contains(Move.DEFECT)) {
            triggered = true;
        }
        return triggered ? Move.DEFECT : Move.COOPERATE;
    }
}
