package com.genglefr.webflux.demo.service;

import com.genglefr.webflux.demo.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public class GenericService<T extends Entity> {
    @Autowired
    CouchbasePagingAndSortingRepository<T, String> genericRepository;

    public T save(T entity) {
        return genericRepository.save(entity);
    }

    public void delete(String id) {
        genericRepository.deleteById(id);
    }

    public Iterable<T> findAll() {
        return genericRepository.findAll(Sort.by("id"));
    }

    public Optional<T> findById(String id) {
        return this.genericRepository.findById(id);
    }
}
