package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public class AlwaysCooperate implements Strategy {
    @Override
    public String name() {
        return "AlwaysCooperate";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        return Move.COOPERATE;
    }
}
