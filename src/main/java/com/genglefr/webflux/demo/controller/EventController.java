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

import java.time.Duration;
import java.util.List;

@RestController
public class EventController {
    @Autowired
    private Publisher<Message<Game>> couchbaseEventPublisher;

    @GetMapping(value = "events/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> events() {
        return Flux.from(this.couchbaseEventPublisher)
                .map(Message::getPayload)
                .delayElements(Duration.ofMillis(500));
    }

    @GetMapping(value = "events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> filteredEvents(@RequestParam(value = "teams", required = false) List<String> teams) {
        return Flux.from(this.couchbaseEventPublisher)
                .map(Message::getPayload)
                .filterWhen(game -> Mono.just(game.isFavorite(teams)))
                .delayElements(Duration.ofMillis(500));
    }
}
