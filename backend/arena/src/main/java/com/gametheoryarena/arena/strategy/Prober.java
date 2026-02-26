package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public class Prober implements Strategy {
    @Override
    public String name() {
        return "Prober";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        int round = selfHistory.size();
        if (round == 0) {
            return Move.DEFECT;
        }
        if (round == 1 || round == 2) {
            return Move.COOPERATE;
        }
        boolean opponentSeemsNaive = opponentHistory.size() >= 3
                && opponentHistory.get(1) == Move.COOPERATE
                && opponentHistory.get(2) == Move.COOPERATE;
        if (opponentSeemsNaive) {
            return Move.DEFECT;
        }
        Move lastOpponent = opponentHistory.get(opponentHistory.size() - 1);
        return lastOpponent;
    }
}
