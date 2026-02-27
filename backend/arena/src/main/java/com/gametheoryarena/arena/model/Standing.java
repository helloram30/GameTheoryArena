package com.gametheoryarena.arena.model;

public record Standing(
        String strategy,
        int matchesPlayed,
        int wins,
        int draws,
        int losses,
        double winRate,
        double totalPoints,
        double averageScorePerRound,
        int pointDifferential
) {
}
