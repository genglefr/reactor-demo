package com.genglefr.webflux.demo.controller;

import com.genglefr.webflux.demo.model.Game;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class EventController {
    private final Flux<Game> events;
    private AtomicInteger counter = new AtomicInteger();

    public EventController(@Autowired Publisher<Message<Game>> gameEventPublisher) {
        this.events = Flux.from(gameEventPublisher)
                .limitRate(1)
                .map(Message::getPayload);
    }

    @GetMapping(value = "event/game", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> events() {
        return this.events;
    }

    @GetMapping(value = "event/fav/game", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> filteredEvents(@RequestParam(value = "fav", required = false) List<String> favorites) {
        return this.events.filterWhen(game -> Mono.just(game.isFavorite(favorites)));
    }
}
