package com.genglefr.webflux.demo.controller;

import com.genglefr.webflux.demo.model.Game;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class EventController {
    private final Flux<Game> events;
    private final FluxProcessor<Integer, Integer> subscriptions;

    public EventController(@Autowired final Publisher<Message<Game>> gameEventPublisher) {
        this.subscriptions = DirectProcessor.<Integer>create();
        final FluxSink<Integer> sink = subscriptions.sink();
        final AtomicInteger counter = new AtomicInteger(0);
        this.events = Flux.from(gameEventPublisher)
                .doOnSubscribe(subscription -> sink.next(counter.incrementAndGet()))
                .doOnCancel(() -> sink.next(counter.decrementAndGet()))
                .map(Message::getPayload);
    }

    @GetMapping(value = "event/subscription/count", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> count() {
        return this.subscriptions;
    }

    @GetMapping(value = "event/game", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> games(@RequestParam(value = "fav", required = false) final List<String> favorites) {
        return this.events.filter(game -> CollectionUtils.isEmpty(favorites) || game.isFavorite(favorites)).delayElements(Duration.ofMillis(200));
    }
}
