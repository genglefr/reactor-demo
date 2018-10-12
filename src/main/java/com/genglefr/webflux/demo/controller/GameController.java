package com.genglefr.webflux.demo.controller;

import com.genglefr.webflux.demo.model.Game;
import com.genglefr.webflux.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping(value = "games")
    public Flux<Game> all() {
        return this.gameService.findAll();
    }

    @DeleteMapping(value = "games")
    public void reset() {
        this.gameService.findAll().doOnNext(game -> this.gameService.save(game.reset()).subscribe()).subscribe();
    }

    @PostMapping(value = "games/random")
    public void randomize() {
        this.gameService.findAll().doOnNext(game -> this.gameService.save(game.randomize()).subscribe()).subscribe();
    }
}
