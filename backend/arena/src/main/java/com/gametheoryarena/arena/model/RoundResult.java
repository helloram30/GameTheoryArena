package com.gametheoryarena.arena.model;

public record RoundResult(
        int round,
        Move playerOneMove,
        Move playerTwoMove,
        int playerOneScore,
        int playerTwoScore
) {
}
