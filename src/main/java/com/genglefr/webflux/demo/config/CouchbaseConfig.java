package com.genglefr.webflux.demo.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.spring.cache.CacheBuilder;
import com.couchbase.client.spring.cache.CouchbaseCacheManager;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
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
    public Publisher<Message<String>> couchbaseEventPublisher() {
        return IntegrationFlows.
                from(Http.inboundChannelAdapter("/event/{id}")
                        .requestMapping(r -> r.methods(HttpMethod.POST)))
                .log(LoggingHandler.Level.INFO)
                .channel(MessageChannels.publishSubscribe())
                .toReactivePublisher();
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
