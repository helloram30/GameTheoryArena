package com.gametheoryarena.arena.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gametheoryarena.arena.model.TournamentResult;
import com.gametheoryarena.arena.service.MatchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TournamentController {
    private final MatchService matchService;

    public TournamentController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/payoff")
    public String payoff() {
        return "Iterated Prisoner's Dilemma payoff: T=5, R=3, P=1, S=0";
    }

    @PostMapping("/tournament")
    public TournamentResult play(@Valid @RequestBody TournamentRequest request) {
        try {
            return matchService.playTournament(request.strategies(), request.rounds(), request.seed());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
