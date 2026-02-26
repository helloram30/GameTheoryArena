package com.gametheoryarena.arena.web;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record MatchRequest(
        @NotBlank String playerOneStrategy,
        @NotBlank String playerTwoStrategy,
        @Min(1) int rounds,
        Long seed
) {
}
