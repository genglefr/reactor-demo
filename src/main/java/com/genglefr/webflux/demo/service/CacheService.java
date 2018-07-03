package com.genglefr.webflux.demo.service;

import com.couchbase.client.spring.cache.CouchbaseCacheManager;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    @Autowired
    private CouchbaseCacheManager couchbaseCacheManager;
    @Autowired
    private Publisher<Message<String>> couchbaseEvents;

    @Bean
    public Subscriber<Message<String>> cacheEvicter() {
        Subscriber<Message<String>> cacheEvicter = new Subscriber<Message<String>>() {
            @Override
            public void onSubscribe(Subscription subscription) {

            }

            @Override
            public void onNext(Message<String> message) {
                for (String cacheName : couchbaseCacheManager.getCacheNames()) {
                    couchbaseCacheManager.getCache(cacheName).evict(message.getPayload());
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };
        couchbaseEvents.subscribe(cacheEvicter);
        return cacheEvicter;
    }
}
