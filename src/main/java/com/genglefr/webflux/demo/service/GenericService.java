package com.genglefr.webflux.demo.service;

import com.genglefr.webflux.demo.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseSortingRepository;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Transactional
public class GenericService<T extends Entity> {
    @Autowired
    ReactiveCouchbaseSortingRepository<T, String> genericRepository;

    public Mono<T> save(T entity) {
        return genericRepository.save(entity);
    }

    public Mono<Void> delete(String id) {
        return genericRepository.deleteById(id);
    }

    public Flux<T> findAll() {
        return genericRepository.findAll(Sort.by("id"));
    }

    public Mono<T> findById(String id) {
        return this.genericRepository.findById(id);
    }
}
