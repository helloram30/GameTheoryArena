package com.gametheoryarena.arena.model;

import java.util.List;

public record TournamentResult(
        int roundsPerMatch,
        List<Standing> standings,
        List<MatchResult> matches
) {
}
