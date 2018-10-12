package com.genglefr.webflux.demo.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.spring.cache.CacheBuilder;
import com.couchbase.client.spring.cache.CouchbaseCacheManager;
import com.genglefr.webflux.demo.model.Game;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.messaging.Message;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableCouchbaseRepositories
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Override
    protected List<String> getBootstrapHosts() {
        return Arrays.asList("localhost");
    }

    @Override
    protected String getBucketName() {
        //bucket name must match an existing user as well
        return "data";
    }

    @Override
    protected String getBucketPassword() {
        //password of the user that has admin rights on the bucket
        return "password";
    }

    @Bean
    public Publisher<Message<Game>> couchbaseEventPublisher() {
        return IntegrationFlows.
                from(WebFlux.inboundChannelAdapter("/event/{id}")
                        .requestMapping(r -> r.methods(HttpMethod.POST)
                                .headers("user-agent=couchbase-eventing/5.5")
                                .consumes("application/json")))
                .transform(gameTransformer())
                .log(LoggingHandler.Level.INFO)
                .channel(MessageChannels.publishSubscribe())
                .toReactivePublisher();
    }

    public JsonToObjectTransformer gameTransformer() {
        return new JsonToObjectTransformer(Game.class);
    }

    @Bean(destroyMethod = "disconnect")
    public Cluster cluster() {
        return CouchbaseCluster.create();
    }

    @Bean(destroyMethod = "close")
    public Bucket bucket() {
        return cluster().openBucket(getBucketName(), getBucketPassword());
    }

    @Bean
    public CouchbaseCacheManager cacheManager() {
        return new CouchbaseCacheManager(CacheBuilder.newInstance(bucket()), "pets", "users", "games");
    }
}
