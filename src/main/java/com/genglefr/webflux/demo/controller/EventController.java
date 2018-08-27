package com.genglefr.webflux.demo.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class EventController {
    @Autowired
    private Publisher<Message<String>> couchbaseEvents;

    @GetMapping(value = "events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> eventSource() {
        return Flux.from(this.couchbaseEvents)
                .delayElements(Duration.ofMillis(100))
                .map(Message::getPayload);
    }
}
