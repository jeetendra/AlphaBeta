package com.jeet.tradingapp.config;

import com.jeet.tradingapp.handler.StockQuoteWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;

@Configuration
public class WebSocketConfig {

    @Bean
    public HandlerMapping webSocketMapping(StockQuoteWebSocketHandler stockQuoteWebSocketHandler) {
        HashMap<String, WebSocketHandler> map = new HashMap<>();
        map.put("/stock", stockQuoteWebSocketHandler);

        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(map);
        mapping.setOrder(10);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
