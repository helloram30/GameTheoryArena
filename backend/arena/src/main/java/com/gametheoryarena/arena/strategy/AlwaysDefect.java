package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public class AlwaysDefect implements Strategy {
    @Override
    public String name() {
        return "AlwaysDefect";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        return Move.DEFECT;
    }
}
