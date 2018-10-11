package com.genglefr.webflux.demo.controller;

import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.messaging.Message;

@Configuration
public class EventController {

    @Bean
    public Publisher<Message<String>> couchbaseEventPublisher() {
        return IntegrationFlows.
                from(WebFlux.inboundChannelAdapter("/event/{id}")
                        .requestMapping(r -> r.methods(HttpMethod.POST)
                                .headers("user-agent=couchbase-eventing/5.5")
                                .consumes("application/json")))
                .log(LoggingHandler.Level.INFO)
                .channel(MessageChannels.flux())
                .toReactivePublisher();
    }

    @Bean
    public IntegrationFlow eventMessages() {
        return IntegrationFlows
                .from(WebFlux.inboundGateway("/events")
                        .requestMapping(m -> m.produces(MediaType.TEXT_EVENT_STREAM_VALUE)))
                .handle((p, h) -> couchbaseEventPublisher())
                .get();
    }
}
