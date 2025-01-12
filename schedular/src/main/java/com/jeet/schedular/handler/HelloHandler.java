package com.jeet.schedular.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class HelloHandler {

    public Mono<ServerResponse> sayHello(ServerRequest request) {
        System.out.println(request.toString());

        return ServerResponse.ok().bodyValue("Hello ji");
    }

    public Mono<ServerResponse> sayHello2(ServerRequest request) {
        System.out.println(request.toString());

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(BodyInserters.fromPublisher(
                        Flux.interval(Duration.ofMillis(1000))
                                .take(5)
                                .map(x -> "Hello " + x),
                        String.class
                ));
    }

    public Mono<ServerResponse> sayHelloWithQP(ServerRequest request) {
        String name = request.queryParam("name").orElse("Guest");
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(BodyInserters.fromPublisher(
                        Flux.interval(Duration.ofMillis(2000))
                                .take(5)
                                .map(i -> "Hello" + name),
                        String.class
                ));
    }

}
