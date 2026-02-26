package com.gametheoryarena.arena.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gametheoryarena.arena.model.MatchResult;
import com.gametheoryarena.arena.service.MatchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/strategies")
    public List<String> listStrategies() {
        return matchService.listStrategies();
    }

    @PostMapping("/match")
    public MatchResult play(@Valid @RequestBody MatchRequest request) {
        try {
            return matchService.play(request.playerOneStrategy(), request.playerTwoStrategy(), request.rounds(),
                    request.seed());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
