package com.gametheoryarena.arena.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.gametheoryarena.arena.model.Standing;
import com.gametheoryarena.arena.model.TournamentResult;
import com.gametheoryarena.arena.strategy.StrategyRegistry;
import com.gametheoryarena.arena.web.CustomStrategyRequest;

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
    void canDisableSelfPlayForPairOnlyRuns() {
        List<String> strategies = List.of("AlwaysDefect", "TitForTat");

        TournamentResult result = matchService.playTournament(strategies, 200, 42L, null, false);

        assertEquals(1, result.matches().size());
        result.standings().forEach(standing -> assertEquals(1, standing.matchesPlayed()));
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

    @Test
    void includesCustomStrategyInTournament() {
        List<CustomStrategyRequest> customStrategies = List.of(new CustomStrategyRequest(
                "MyRuleBot",
                CustomStrategyRequest.FirstMove.COOPERATE,
                CustomStrategyRequest.ResponseMode.MIRROR_LAST,
                0.0));

        TournamentResult result = matchService.playTournament(null, 100, 42L, customStrategies);

        assertTrue(result.standings().stream().anyMatch(standing -> standing.strategy().equals("MyRuleBot")));
    }

    @Test
    void rejectsCustomStrategyNameConflict() {
        List<CustomStrategyRequest> customStrategies = List.of(new CustomStrategyRequest(
                "TitForTat",
                CustomStrategyRequest.FirstMove.COOPERATE,
                CustomStrategyRequest.ResponseMode.MIRROR_LAST,
                0.0));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> matchService.playTournament(null, 100, 42L, customStrategies));
        assertTrue(ex.getMessage().contains("already exists"));
    }
}
