package com.genglefr.webflux.demo.controller;

import com.genglefr.webflux.demo.model.Game;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.webflux.dsl.WebFlux;
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
    private Publisher<Message<Game>> gameEventPublisher;

    @Bean
    public Publisher<Message<Game>> gameEventPublisher() {
        return IntegrationFlows.
                from(WebFlux.inboundChannelAdapter("/event/{id}")
                        .requestMapping(r -> r.methods(HttpMethod.POST)
                                .headers("user-agent=couchbase-eventing/5.5")
                                .params("class=" + Game.class.getName())
                                .consumes("application/json")))
                .transform(new JsonToObjectTransformer(Game.class))
                .log(LoggingHandler.Level.INFO)
                .channel(MessageChannels.publishSubscribe())
                .toReactivePublisher();
    }

    @GetMapping(value = "event/game", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> events() {
        return Flux.from(this.gameEventPublisher)
                .map(Message::getPayload)
                .delayElements(Duration.ofMillis(500));
    }

    @GetMapping(value = "event/fav/game", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> filteredEvents(@RequestParam(value = "fav", required = false) List<String> favorites) {
        return Flux.from(this.gameEventPublisher)
                .map(Message::getPayload)
                .filterWhen(game -> Mono.just(game.isFavorite(favorites)))
                .delayElements(Duration.ofMillis(500));
    }
}
