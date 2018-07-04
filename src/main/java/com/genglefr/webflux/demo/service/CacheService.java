package com.genglefr.webflux.demo.service;

import com.couchbase.client.spring.cache.CouchbaseCacheManager;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Service
public class CacheService {
    @Autowired
    private CouchbaseCacheManager couchbaseCacheManager;
    @Autowired
    private Publisher<Message<String>> couchbaseEvents;

    //@PostConstruct
    public void cacheEvicter() {
        Flux.from(this.couchbaseEvents)
                .map(Message::getPayload).subscribe(s -> {
            handle(s);
        });
    }

    private void handle(String message) {
        System.out.println(message);
        for (String cacheName : couchbaseCacheManager.getCacheNames()) {
            couchbaseCacheManager.getCache(cacheName).evict(message);
        }
    }
}
