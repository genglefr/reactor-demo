package com.genglefr.webflux.demo.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class EventController {
    @Autowired
    private Publisher<Message<String>> couchbaseEventPublisher;

    @GetMapping(value = "events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> eventSource(@RequestParam(value = "delay", defaultValue = "1000") Long delay) {
        return Flux.from(this.couchbaseEventPublisher)
                .delayElements(Duration.ofMillis(delay))
                .map(Message::getPayload);
    }
}
