package com.gametheoryarena.arena.service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gametheoryarena.arena.game.MatchEngine;
import com.gametheoryarena.arena.model.MatchResult;
import com.gametheoryarena.arena.model.Standing;
import com.gametheoryarena.arena.model.TournamentResult;
import com.gametheoryarena.arena.strategy.Strategy;
import com.gametheoryarena.arena.strategy.StrategyRegistry;

@Service
public class MatchService {
    private final StrategyRegistry strategyRegistry;
    private final MatchEngine matchEngine = new MatchEngine();

    public MatchService(StrategyRegistry strategyRegistry) {
        this.strategyRegistry = strategyRegistry;
    }

    public List<String> listStrategies() {
        return strategyRegistry.availableStrategies();
    }

    public MatchResult play(String strategyOne, String strategyTwo, int rounds, Long seed) {
        if (rounds < 1) {
            throw new IllegalArgumentException("Rounds must be >= 1");
        }
        Random baseRandom = seed == null ? new Random() : new Random(seed);
        Strategy playerOne = strategyRegistry.create(strategyOne, new Random(baseRandom.nextLong()));
        Strategy playerTwo = strategyRegistry.create(strategyTwo, new Random(baseRandom.nextLong()));
        return matchEngine.play(playerOne, playerTwo, rounds);
    }

    public TournamentResult playTournament(List<String> requestedStrategies, int rounds, Long seed) {
        if (rounds < 1) {
            throw new IllegalArgumentException("Rounds must be >= 1");
        }
        List<String> strategies = requestedStrategies == null || requestedStrategies.isEmpty()
                ? strategyRegistry.availableStrategies()
                : requestedStrategies.stream().distinct().toList();
        if (strategies.size() < 2) {
            throw new IllegalArgumentException("Tournament requires at least 2 strategies");
        }
        strategies.forEach(name -> {
            if (!strategyRegistry.hasStrategy(name)) {
                throw new IllegalArgumentException("Unknown strategy: " + name);
            }
        });

        Map<String, MutableStanding> standingMap = strategies.stream()
                .collect(Collectors.toMap(name -> name, ignored -> new MutableStanding()));
        List<MatchResult> matches = new java.util.ArrayList<>();
        Random baseRandom = seed == null ? new Random() : new Random(seed);

        for (int i = 0; i < strategies.size(); i++) {
            for (int j = i + 1; j < strategies.size(); j++) {
                String strategyOneName = strategies.get(i);
                String strategyTwoName = strategies.get(j);

                Strategy playerOne = strategyRegistry.create(strategyOneName, new Random(baseRandom.nextLong()));
                Strategy playerTwo = strategyRegistry.create(strategyTwoName, new Random(baseRandom.nextLong()));
                MatchResult result = matchEngine.play(playerOne, playerTwo, rounds);
                matches.add(result);

                MutableStanding one = standingMap.get(strategyOneName);
                MutableStanding two = standingMap.get(strategyTwoName);
                one.matchesPlayed++;
                two.matchesPlayed++;
                one.totalPoints += result.playerOneTotal();
                two.totalPoints += result.playerTwoTotal();
                one.pointDifferential += result.playerOneTotal() - result.playerTwoTotal();
                two.pointDifferential += result.playerTwoTotal() - result.playerOneTotal();

                if (result.playerOneTotal() > result.playerTwoTotal()) {
                    one.wins++;
                    two.losses++;
                } else if (result.playerTwoTotal() > result.playerOneTotal()) {
                    two.wins++;
                    one.losses++;
                } else {
                    one.draws++;
                    two.draws++;
                }
            }
        }

        List<Standing> standings = standingMap.entrySet().stream()
                .map(entry -> toStanding(entry.getKey(), entry.getValue()))
                .sorted((left, right) -> {
                    int compareWinRate = Double.compare(right.winRate(), left.winRate());
                    if (compareWinRate != 0) {
                        return compareWinRate;
                    }
                    int comparePointDiff = Integer.compare(right.pointDifferential(), left.pointDifferential());
                    if (comparePointDiff != 0) {
                        return comparePointDiff;
                    }
                    int compareTotalPoints = Integer.compare(right.totalPoints(), left.totalPoints());
                    if (compareTotalPoints != 0) {
                        return compareTotalPoints;
                    }
                    return left.strategy().compareTo(right.strategy());
                })
                .toList();

        return new TournamentResult(rounds, standings, matches);
    }

    private Standing toStanding(String strategy, MutableStanding standing) {
        double winRate = standing.matchesPlayed == 0
                ? 0.0
                : (standing.wins + (standing.draws * 0.5)) / standing.matchesPlayed;
        return new Standing(
                strategy,
                standing.matchesPlayed,
                standing.wins,
                standing.draws,
                standing.losses,
                winRate,
                standing.totalPoints,
                standing.pointDifferential);
    }

    private static final class MutableStanding {
        private int matchesPlayed;
        private int wins;
        private int draws;
        private int losses;
        private int totalPoints;
        private int pointDifferential;
    }
}
