package com.genglefr.webflux.demo.controller;

import com.genglefr.webflux.demo.model.Game;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class EventController {
    private final Flux<Game> events;
    private final FluxSink<Integer> sink;
    private final FluxProcessor<Integer, Integer> processor;
    private static final AtomicInteger counter = new AtomicInteger(0);

    public EventController(@Autowired Publisher<Message<Game>> gameEventPublisher) {
        this.processor = DirectProcessor.<Integer>create();
        this.sink = processor.sink();
        this.events = Flux.from(gameEventPublisher)
                .doOnSubscribe(subscription -> this.sink.next(counter.incrementAndGet()))
                .doOnCancel(() -> this.sink.next(counter.decrementAndGet()))
                .map(Message::getPayload);
    }

    @GetMapping(value = "event/game", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> events() {
        return this.events.delayElements(Duration.ofMillis(200));
    }

    @GetMapping(value = "event/fav/game", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> filteredEvents(@RequestParam(value = "fav", required = false) List<String> favorites) {
        return this.events.filterWhen(game -> Mono.just(game.isFavorite(favorites))).delayElements(Duration.ofMillis(200));
    }

    @GetMapping(value = "event/subscription/count", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> count() {
        return this.processor.share();
    }
}
