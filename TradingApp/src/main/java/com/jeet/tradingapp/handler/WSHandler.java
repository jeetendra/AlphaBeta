package com.jeet.tradingapp.handler;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WSHandler implements WebSocketHandler {

    private final Map<String, Sinks.Many<String>> sinks = new ConcurrentHashMap<>();

    public Mono<String> triggerMessage(String message, String sessionId) {
        String messageToSend = message != null ? message : "Default message from API!";
        if (sessionId == null || sessionId.isEmpty()) {
            sinks.values().forEach(sink -> sink.tryEmitNext(messageToSend));
            return Mono.just("Message sent to all connected clients");
        }
        Sinks.Many<String> sink = sinks.get(sessionId);
        if (sink != null) {
            sink.tryEmitNext(messageToSend);
            return Mono.just("Message sent to session: " + sessionId);
        } else {
            return Mono.just("No WebSocket session found.");
        }
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
        sinks.put(session.getId(), sink);

        // Handle incoming messages (optional, if you need bidirectional communication)
        Flux<WebSocketMessage> input = session.receive();

        Flux<String> stringFlux = input.map(WebSocketMessage::getPayloadAsText)
                .doOnNext(message -> System.out.println("Received: " + message));

        // Send messages from the sink to the WebSocket
        Flux<WebSocketMessage> output = sink.asFlux()
                .map(session::textMessage);

        // Keep the connection alive until the session is closed or an error occurs
        return session.send(output)
                .mergeWith(stringFlux.then()) // Merge input handling if needed
                .doFinally(signalType -> {
                    System.out.println("Session closed: " + session.getId() + ", Signal: " + signalType);
                    sinks.remove(session.getId()); // Clean up the sink
                })
                .then();
    }
}
