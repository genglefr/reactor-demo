package com.genglefr.webflux.demo.controller;

import com.genglefr.webflux.demo.integration.ReactiveEventRepository;
import com.genglefr.webflux.demo.service.EventService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController

public class EventController {
    @Autowired
    private ReactiveEventRepository reactiveEventRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private Publisher<Message<String>> couchbaseEvents;

    /*@GetMapping(value = "events")
    @ResponseBody
    public Flux<Event> events() {
        return this.reactiveEventRepository.findByDateGreaterThan(new Date()).delayElements(Duration.ofMillis(1000));
    }

    @GetMapping(value = "users/events")
    @ResponseBody
    public Flux<Event> userEvents() {
        return this.reactiveEventRepository.findByResourceTypeAndDateGreaterThan(User.class.getSimpleName(), new Date()).delayElements(Duration.ofMillis(1000));
    }

    /*@PostMapping(value = "events/{id}")
    public void handle(@PathVariable("id") final String id, @RequestBody Entity entity) {
        this.eventService.handleEvent(id, entity);
    }*/

    @GetMapping(value = "events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> eventSource() {
        return Flux.from(this.couchbaseEvents)
                .map(Message::getPayload);
    }

}
