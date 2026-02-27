package com.gametheoryarena.arena.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.gametheoryarena.arena.model.Standing;
import com.gametheoryarena.arena.model.TournamentResult;
import com.gametheoryarena.arena.strategy.StrategyRegistry;

class MatchServiceTournamentTest {

    private final MatchService matchService = new MatchService(new StrategyRegistry());

    @Test
    void includesSelfPlayInRoundRobinSchedule() {
        List<String> strategies = List.of("AlwaysDefect", "TitForTat", "AlwaysCooperate");

        TournamentResult result = matchService.playTournament(strategies, 200, 42L);

        assertEquals(6, result.matches().size());
        result.standings().forEach(standing -> assertEquals(3, standing.matchesPlayed()));
    }

    @Test
    void ranksByAverageScorePerRound() {
        List<String> strategies = List.of("AlwaysDefect", "TitForTat", "GrimTrigger", "WinStayLoseShift", "TitForTwoTats");

        TournamentResult result = matchService.playTournament(strategies, 200, 42L);
        List<Standing> standings = result.standings();

        assertEquals("AlwaysDefect", standings.get(standings.size() - 1).strategy());
        for (int i = 1; i < standings.size(); i++) {
            assertTrue(standings.get(i - 1).averageScorePerRound() >= standings.get(i).averageScorePerRound());
        }
    }
}
