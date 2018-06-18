package com.genglefr.webflux.demo.service;

import com.genglefr.webflux.demo.integration.EventRepository;
import com.genglefr.webflux.demo.model.Entity;
import com.genglefr.webflux.demo.model.Event;
import com.genglefr.webflux.demo.model.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class GenericService<T extends Entity> {
    @Autowired
    MongoRepository<T, String> genericRepository;
    @Autowired
    EventRepository eventRepository;

    public T save(T entity) {
        OperationType operationType = entity.getId() == null ? OperationType.C : OperationType.U;
        T returned = genericRepository.save(entity);
        eventRepository.save(new Event(entity, operationType));
        return returned;
    }

    public void delete(String id) {
        Optional<T> result = genericRepository.findById(id);
        if (result.isPresent()) {
            T entity = result.get();
            genericRepository.delete(entity);
            this.eventRepository.save(new Event(entity, OperationType.D));
        } else {
            throw new IllegalArgumentException("Entity not found for id: " + id);
        }
    }

    public List<T> findAll() {
        return genericRepository.findAll();
    }
}
