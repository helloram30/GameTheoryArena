package com.gametheoryarena.arena.strategy;

import java.util.List;
import java.util.Random;

import com.gametheoryarena.arena.model.Move;
import com.gametheoryarena.arena.web.CustomStrategyRequest;

public class RuleBasedStrategy implements Strategy {
    private final String strategyName;
    private final Move firstMove;
    private final CustomStrategyRequest.ResponseMode responseMode;
    private final double forgivenessProbability;
    private final Random random;

    public RuleBasedStrategy(
            String strategyName,
            Move firstMove,
            CustomStrategyRequest.ResponseMode responseMode,
            double forgivenessProbability,
            Random random) {
        this.strategyName = strategyName;
        this.firstMove = firstMove;
        this.responseMode = responseMode;
        this.forgivenessProbability = forgivenessProbability;
        this.random = random;
    }

    @Override
    public String name() {
        return strategyName;
    }

    @Override
    public Move nextMove(List<Move> selfHistory, List<Move> opponentHistory) {
        Move baseMove = decideBaseMove(opponentHistory);
        if (baseMove == Move.DEFECT && forgivenessProbability > 0.0 && random.nextDouble() < forgivenessProbability) {
            return Move.COOPERATE;
        }
        return baseMove;
    }

    private Move decideBaseMove(List<Move> opponentHistory) {
        if (opponentHistory.isEmpty()) {
            return firstMove;
        }

        return switch (responseMode) {
            case ALWAYS_COOPERATE -> Move.COOPERATE;
            case ALWAYS_DEFECT -> Move.DEFECT;
            case MIRROR_LAST -> opponentHistory.get(opponentHistory.size() - 1);
            case DEFECT_AFTER_ONE -> opponentHistory.get(opponentHistory.size() - 1) == Move.DEFECT
                    ? Move.DEFECT
                    : Move.COOPERATE;
            case DEFECT_AFTER_TWO -> {
                if (opponentHistory.size() < 2) {
                    yield Move.COOPERATE;
                }
                Move last = opponentHistory.get(opponentHistory.size() - 1);
                Move prev = opponentHistory.get(opponentHistory.size() - 2);
                yield (last == Move.DEFECT && prev == Move.DEFECT) ? Move.DEFECT : Move.COOPERATE;
            }
        };
    }
}
