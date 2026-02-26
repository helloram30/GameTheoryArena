package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public interface Strategy {
    String name();

    Move nextMove(List<Move> selfHistory, List<Move> opponentHistory);
}
