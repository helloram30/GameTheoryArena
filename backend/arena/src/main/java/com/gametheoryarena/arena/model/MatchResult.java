package com.gametheoryarena.arena.model;

import java.util.List;

public record MatchResult(
        String playerOneStrategy,
        String playerTwoStrategy,
        int playerOneTotal,
        int playerTwoTotal,
        List<RoundResult> rounds
) {
}
