package com.jeet.tradingapp.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeet.tradingapp.model.StockQuote;
import org.apache.kafka.common.errors.SerializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StockQuoteDeserializerTest {

    @Test
    void testDeserialize() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        StockQuoteDeserializer deserializer = new StockQuoteDeserializer();

        String json = "{\"symbol\":\"AAPL\",\"price\":175.50,\"volume\":15000,\"timestamp\":\"2024-07-13T12:00:00Z\"}";
        byte[] data = json.getBytes();

        StockQuote quote = deserializer.deserialize("test-topic", data);


        assertNotNull(quote);
        assertEquals("AAPL", quote.getSymbol());
        assertEquals(new BigDecimal("175.50"), quote.getPrice());
        assertEquals(15000L, quote.getVolume());
        assertEquals(Instant.parse("2024-07-13T12:00:00Z"), quote.getTimestamp());

    }

    @Test
    void testDeserializeInvalidJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Important: Register JavaTimeModule
        StockQuoteDeserializer deserializer = new StockQuoteDeserializer();
        byte[] data = "invalid json".getBytes();

        assertThrows(SerializationException.class, () -> deserializer.deserialize("test-topic", data));
    }

    @Test
    void testDeserializeNullData() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Important: Register JavaTimeModule
        StockQuoteDeserializer deserializer = new StockQuoteDeserializer();

        assertNull(deserializer.deserialize("test-topic", null));
    }

//    @Test
//    void testDeserializeMissingField() throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules(); // Important: Register JavaTimeModule
//        StockQuoteDeserializer deserializer = new StockQuoteDeserializer();
//        String json = "{\"symbol\":\"AAPL\",\"price\":175.50}"; // Missing volume and timestamp
//        byte[] data = json.getBytes();
//
//        assertThrows(com.fasterxml.jackson.databind.exc.MismatchedInputException.class, () -> deserializer.deserialize("test-topic", data));
//
//    }

    @Test
    void testDeserializeWrongType() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Important: Register JavaTimeModule
        StockQuoteDeserializer deserializer = new StockQuoteDeserializer();
        String json = "{\"symbol\":\"AAPL\",\"price\":\"not a number\",\"volume\":15000,\"timestamp\":\"2024-07-13T12:00:00Z\"}"; // price is string
        byte[] data = json.getBytes();

        assertThrows(SerializationException.class, () -> deserializer.deserialize("test-topic", data));

    }

}