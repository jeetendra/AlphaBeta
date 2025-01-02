package com.jeet.tradingapp.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeet.tradingapp.model.StockQuote;
import com.jeet.tradingapp.util.MapperUtil;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class StockQuoteDeserializer implements Deserializer<StockQuote> {

    private static final Logger logger = LoggerFactory.getLogger(StockQuoteDeserializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
//        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public StockQuote deserialize(String topic, byte[] data) {
        System.out.println("StockQuote deserialized");
        if (data == null) {
            return null; // Handle null data gracefully
        }

        String message = new String(data, StandardCharsets.UTF_8); // Log the raw message
        logger.error("Raw message from kafka: {}", message);



        try {
            return MapperUtil.getMapper().readValue(data, StockQuote.class);
        } catch (IOException e) {
            System.err.println("Error deserializing StockQuote: " + e.getMessage());
            throw new SerializationException("Error deserializing StockQuote", e);
        }
    }

    @Override
    public void close() {
//        Deserializer.super.close();
    }
}
