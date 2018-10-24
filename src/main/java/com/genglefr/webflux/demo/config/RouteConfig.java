package com.genglefr.webflux.demo.config;

import com.genglefr.webflux.demo.model.Game;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.messaging.Message;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RouteConfig {

    @Autowired
    HttpHandler httpHandler;

    @Bean
    public NettyReactiveWebServerFactory nettyReactiveWebServerFactory(@Value("${http.port:8080}") int port) {
        NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory(port);
        factory.getWebServer(this.httpHandler).start();
        return factory;
    }

    @Bean
    public Publisher<Message<Game>> gameEventPublisher() {
        return IntegrationFlows.
                from(WebFlux.inboundChannelAdapter("/event/{id}")
                        .requestMapping(r -> r.methods(HttpMethod.POST)
                                .headers("user-agent=couchbase-eventing/5.5")
                                .params("class=" + Game.class.getName())
                                .consumes("application/json")))
                .transform(new JsonToObjectTransformer(Game.class))
                .channel(MessageChannels.publishSubscribe())
                .toReactivePublisher();
    }

    @Bean
    public RouterFunction<ServerResponse> indexRouter(@Value("file:src/main/static/index.html") final Resource indexHtml) {
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(indexHtml));
    }

}
