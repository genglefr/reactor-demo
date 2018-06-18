package com.genglefr.webflux.demo.controller;

import com.genglefr.webflux.demo.integration.ReactiveEventRepository;
import com.genglefr.webflux.demo.model.Event;
import com.genglefr.webflux.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Date;

@RestController
public class EventController {
    @Autowired
    private ReactiveEventRepository reactiveEventRepository;
    @GetMapping(value = "events")
    @ResponseBody
    public Flux<Event> events() {
        return this.reactiveEventRepository.findByDateGreaterThan(new Date()).delayElements(Duration.ofMillis(1000));
    }
    @GetMapping(value = "users/events")
    @ResponseBody
    public Flux<Event> userEvents() {
        return this.reactiveEventRepository.findByResourceTypeAndDateGreaterThan(User.class.getSimpleName(), new Date()).delayElements(Duration.ofMillis(1000));
    }
}
