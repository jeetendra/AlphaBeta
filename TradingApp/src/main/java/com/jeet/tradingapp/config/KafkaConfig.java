package com.jeet.tradingapp.config;

import com.jeet.tradingapp.model.StockQuote;
import com.jeet.tradingapp.serializer.StockQuoteDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public Flux<StockQuote> kafkaFlux() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094"); // Replace with your Kafka brokers
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "stock-ticker-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "com.jeet.tradingapp.serializer.StockQuoteDeserializer"); // Custom deserializer
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Important for consuming from the beginning

        ReceiverOptions<String, StockQuote> receiverOptions = ReceiverOptions.create(props);
        receiverOptions = receiverOptions.subscription(Collections.singleton("stock-quotes"));

        return KafkaReceiver.create(receiverOptions)
                .receive()
                .map(ReceiverRecord::value);
    }
}
