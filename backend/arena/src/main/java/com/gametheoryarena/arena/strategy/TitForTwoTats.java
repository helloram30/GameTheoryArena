package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public class TitForTwoTats implements Strategy {
    @Override
    public String name() {
        return "TitForTwoTats";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        if (opponentHistory.size() < 2) {
            return Move.COOPERATE;
        }
        Move last = opponentHistory.get(opponentHistory.size() - 1);
        Move secondLast = opponentHistory.get(opponentHistory.size() - 2);
        if (last == Move.DEFECT && secondLast == Move.DEFECT) {
            return Move.DEFECT;
        }
        return Move.COOPERATE;
    }
}
