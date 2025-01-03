package com.jeet.tradingapp.config;

import com.jeet.tradingapp.handler.StockQuoteWebSocketHandler;
import com.jeet.tradingapp.handler.WSHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {

    @Bean
    public HandlerMapping webSocketMappingQuotes(StockQuoteWebSocketHandler stockQuoteWebSocketHandler) {
        HashMap<String, WebSocketHandler> map = new HashMap<>();
        map.put("/quotes", stockQuoteWebSocketHandler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        mapping.setOrder(10);
        return mapping;
    }

    @Bean
    public HandlerMapping wsHandlerMappingRoot(WSHandler webSocketHandler) {
        System.out.println("Handler Mapping called X");
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/tempo", webSocketHandler);
//        map.put("/tempo", session -> {
//            return session.send(Flux.just("apple").map(session::textMessage)).then();
//        });
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(2); // Important for WebSocket handling
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }

    @Bean
    public HandlerMapping webSocketHandlerMapping() {
        System.out.println("Handler Mapping called");
        Map<String, WebSocketHandler> map = new HashMap<>();

        map.put("/echo", session -> session.receive()
                .map(webSocketMessage -> "Echo: " + webSocketMessage.getPayloadAsText())
                .map(session::textMessage)
                .as(session::send)
                .doOnError(error -> System.err.println("WebSocket error: " + error.getMessage()))
                .doFinally(signalType -> System.out.println("WebSocket connection closed: " + signalType)));

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setUrlMap(map);
        handlerMapping.setOrder(1);
        return handlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        System.out.println("Handler Adapter called");
        return new WebSocketHandlerAdapter();
    }


}
