package com.jeet.websocketclient;

import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

//@SpringBootApplication
public class WebSocketClientApplication {

    public static void main(String[] args) {
//		SpringApplication.run(WebSocketClientApplication.class, args);

        ReactorNettyWebSocketClient
                client = new ReactorNettyWebSocketClient();
        String uri = "ws://localhost:8080/quotes";

        client.execute(URI
                        .create(uri), session ->
                        session.send(Mono.just(session.textMessage("start")))
                                .thenMany(session.receive().take(5))
                                .doOnNext(message -> {
                                    System.out.println("Received: " + message.getPayloadAsText());
                                    // Parse JSON here if needed
                                })
                                .then()
                )
                .block(Duration.ofSeconds(10));
    }

}
