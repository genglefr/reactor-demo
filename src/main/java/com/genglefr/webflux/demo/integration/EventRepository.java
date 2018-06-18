package com.genglefr.webflux.demo.integration;

import com.genglefr.webflux.demo.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
}
