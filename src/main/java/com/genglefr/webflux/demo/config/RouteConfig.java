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
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@EnableScheduling
public class RouteConfig {

    @Autowired
    HttpHandler httpHandler;

    @Bean
    public NettyReactiveWebServerFactory nettyReactiveWebServerFactory(@Value("${http.port:8080}") Integer port) {
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
    public RouterFunction<ServerResponse> indexRouter(@Value("file:src/main/static/index.html") final Resource indexHtml, @Value("${server.port:8443}") Integer port) throws UnknownHostException {
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(indexHtml))
                .andRoute(GET("/deployment-info"), request -> ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(Map.of("applicationHostAddress", getHostAddress(), "applicationPort", port)), Map.class));
    }

    private String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
