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
        this.gameService.findAll().doOnNext(game -> reset(game)).subscribe();
    }

    private void reset(Game game) {
        if (game.hasScored()) {
            game.reset();
            this.gameService.save(game).subscribe();
        }
    }

    @PostMapping(value = "games/random")
    public void randomize() {
        this.gameService.findAll().doOnNext(game -> randomize(game)).subscribe();
    }

    private void randomize(Game game) {
        game.randomize();
        this.gameService.save(game).subscribe();
    }

    @PostMapping(value = "game")
    public void create(@RequestBody Game game) {
        this.gameService.save(game).subscribe();
    }

    @PostMapping(value = "game/{id}")
    public void update(@RequestBody Game game, @PathVariable("id") String id) {
        game.setId(id);
        this.gameService.save(game).subscribe();
    }

    @DeleteMapping(value = "game/{id}")
    public void delete(@PathVariable("id") String id) {
        this.gameService.delete(id).subscribe();
    }
}
