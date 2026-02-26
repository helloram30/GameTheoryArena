package com.gametheoryarena.arena.strategy;

import java.util.List;

import com.gametheoryarena.arena.model.Move;

public class WinStayLoseShift implements Strategy {
    @Override
    public String name() {
        return "WinStayLoseShift";
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        if (selfHistory.isEmpty()) {
            return Move.COOPERATE;
        }
        Move lastSelf = selfHistory.get(selfHistory.size() - 1);
        Move lastOpponent = opponentHistory.get(opponentHistory.size() - 1);
        boolean lastRoundWasWin = (lastSelf == Move.COOPERATE && lastOpponent == Move.COOPERATE)
                || (lastSelf == Move.DEFECT && lastOpponent == Move.DEFECT);
        if (lastRoundWasWin) {
            return lastSelf;
        }
        return lastSelf == Move.COOPERATE ? Move.DEFECT : Move.COOPERATE;
    }
}
