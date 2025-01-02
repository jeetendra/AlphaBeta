package com.jeet.tradingapp.handler;

import com.jeet.tradingapp.model.StockQuote;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class StockQuoteWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(StockQuoteWebSocketHandler.class);

    private final Flux<StockQuote> stockQuoteFlux;

    public StockQuoteWebSocketHandler(Flux<StockQuote> stockQuoteFlux) {
        this.stockQuoteFlux = stockQuoteFlux;
    }

//    Code to debug Kafka
    @PostConstruct
    public void checkKafka() {
//        if (stockQuoteFlux != null) {
//            logger.info("kafkaFlux bean is created: {}", stockQuoteFlux);
//        } else {
//            logger.error("kafkaFlux bean is NOT created!");
//        }
//
//        stockQuoteFlux.subscribe(
//                quote -> logger.info("Consumed from Kafka (PostConstruct): {}", quote),
//                error -> logger.error("Error consuming from Kafka:", error),
//                () -> logger.info("Kafka consumption completed")
//        );
//
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        System.out.println("OOOOOOOOOO");
        return session.send(
                stockQuoteFlux
                        .doOnNext(quote -> logger.info("Received from Kafka: {}", quote))
                        .map(
                quote -> session.textMessage(quote.toString())
        )).then();
    }
}
