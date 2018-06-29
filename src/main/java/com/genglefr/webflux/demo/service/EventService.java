package com.genglefr.webflux.demo.service;

import com.genglefr.webflux.demo.integration.EventRepository;
import com.genglefr.webflux.demo.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventService {
    @Autowired
    EventRepository eventRepository;

    public void save(Event event){
        this.eventRepository.save(event);
    }
}
