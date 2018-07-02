package com.genglefr.webflux.demo.service;

import com.genglefr.webflux.demo.integration.EventRepository;
import com.genglefr.webflux.demo.model.*;
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

    public void handleEvent(String id, Entity entity){
        if (entity == null) {
            final String resourceType = Pet.class.getSimpleName();
            save(new Event(new EmptyEntity(id, Pet.class.getSimpleName()), OperationType.D));
        } else {
            save(new Event(entity, OperationType.U));
        }
    }
}
