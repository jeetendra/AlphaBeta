package com.jeet.chatconsumer.config;

import com.jeet.chatconsumer.handler.ChatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {
    @Bean
    public HandlerMapping webSocketHandlerMapping(ChatWebSocketHandler  chatHandler) {
        System.out.println("WS MAPPING");
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/chat", chatHandler);

        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setUrlMap(map);
        handlerMapping.setOrder(-1);

        return handlerMapping;
    }
}
