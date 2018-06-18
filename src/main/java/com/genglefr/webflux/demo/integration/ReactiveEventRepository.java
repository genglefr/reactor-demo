package com.genglefr.webflux.demo.integration;

import com.genglefr.webflux.demo.model.Event;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

import java.util.Date;

public interface ReactiveEventRepository extends ReactiveMongoRepository<Event, String> {

    @Tailable
    Flux<Event> findByDateGreaterThan(Date date);

    @Tailable
    Flux<Event> findByResourceTypeAndDateGreaterThan(String resourceType, Date date);
}
