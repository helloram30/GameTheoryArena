package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public class ContriteTitForTat implements Strategy {
    private boolean inBadStanding = false;

    @Override
    public String name() {
        return "ContriteTitForTat";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        if (!selfHistory.isEmpty()) {
            Move lastSelf = selfHistory.get(selfHistory.size() - 1);
            Move lastOpponent = opponentHistory.get(opponentHistory.size() - 1);
            if (lastSelf == Move.DEFECT && lastOpponent == Move.COOPERATE) {
                inBadStanding = true;
            }
            if (lastOpponent == Move.DEFECT) {
                inBadStanding = false;
            }
        }
        if (selfHistory.isEmpty()) {
            return Move.COOPERATE;
        }
        if (inBadStanding) {
            return Move.COOPERATE;
        }
        Move lastOpponent = opponentHistory.get(opponentHistory.size() - 1);
        return lastOpponent;
    }
}
