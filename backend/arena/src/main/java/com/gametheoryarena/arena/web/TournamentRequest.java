package com.gametheoryarena.arena.web;

import java.util.List;

import jakarta.validation.constraints.Min;

public record TournamentRequest(
        List<String> strategies,
        @Min(1) int rounds,
        Long seed
) {
}
