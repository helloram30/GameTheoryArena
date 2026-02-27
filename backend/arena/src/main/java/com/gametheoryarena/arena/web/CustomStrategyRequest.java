package com.gametheoryarena.arena.web;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomStrategyRequest(
        @NotBlank String name,
        @NotNull FirstMove firstMove,
        @NotNull ResponseMode responseMode,
        @DecimalMin("0.0") @DecimalMax("1.0") double forgivenessProbability
) {
    public enum FirstMove {
        COOPERATE,
        DEFECT
    }

    public enum ResponseMode {
        MIRROR_LAST,
        DEFECT_AFTER_ONE,
        DEFECT_AFTER_TWO,
        ALWAYS_COOPERATE,
        ALWAYS_DEFECT
    }
}
