package com.genglefr.webflux.demo.controller;

import com.genglefr.webflux.demo.model.Game;
import com.genglefr.webflux.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping(value = "games")
    public Iterable<Game> all() {
        return this.gameService.findAll();
    }

    @PostMapping(value = "game")
    public void create(@RequestBody Game game) {
        this.gameService.save(game);
    }

    @PostMapping(value = "game/{id}")
    public void update(@RequestBody Game game, @PathVariable("id") String id) {
        game.setId(id);
        this.gameService.save(game);
    }

    @DeleteMapping(value = "game/{id}")
    public void delete(@PathVariable("id") String id) {
        this.gameService.delete(id);
    }
}
