package com.jeet.chatconsumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.*;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Testcontainers
@SpringBootTest
class ChatConsumerApplicationTests {

    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("bitnami/kafka:3.9");
    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:15-alpine");

    @Container
    private static final GenericContainer<?> kafka = new GenericContainer<>(KAFKA_IMAGE)
            .withExposedPorts(9092, 9094)
            .withEnv("KAFKA_CFG_NODE_ID", "0")
            .withEnv("KAFKA_CFG_PROCESS_ROLES", "controller,broker")
            .withEnv("KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP", "CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,EXTERNAL:PLAINTEXT")
            .withEnv("KAFKA_CFG_CONTROLLER_QUORUM_VOTERS", "0@localhost:9093")
            .withEnv("KAFKA_CFG_CONTROLLER_LISTENER_NAMES", "CONTROLLER")
            .withEnv("KAFKA_CFG_LISTENERS", "PLAINTEXT://:9092,CONTROLLER://:9093,EXTERNAL://:9094")
            .withEnv("KAFKA_CFG_ADVERTISED_LISTENERS", "PLAINTEXT://localhost:9092,EXTERNAL://localhost:9094")
            .waitingFor(Wait.forListeningPort());

    @Container
    private static final GenericContainer<?> postgres = new GenericContainer<>(POSTGRES_IMAGE)
            .withExposedPorts(5432)
            .withEnv("POSTGRES_USER", "testuser")
            .withEnv("POSTGRES_PASSWORD", "testpass")
            .withEnv("POSTGRES_DB", "testdb")
            .waitingFor(Wait.forListeningPort());

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // Kafka properties
        var bootstrapUrl = kafka.getHost() + ":" + kafka.getMappedPort(9094);
        registry.add("spring.kafka.bootstrap-servers", () -> bootstrapUrl);
        // Below line is to debug kafka
        kafka.withLogConsumer(outputFrame -> System.out.print(outputFrame.getUtf8String()));

        // Postgres properties
        var postgresHost = postgres.getHost();
        var postgresPort = postgres.getMappedPort(5432);
        registry.add("spring.datasource.url", () -> "jdbc:postgresql://${postgresHost}:${postgresPort}/testdb");
        registry.add("spring.datasource.username", () -> "testuser");
        registry.add("spring.datasource.password", () -> "testpass");
    }

    @Test
    void testDB() throws SQLException {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/testdb",
                "testuser", "testpass");
             Statement statement = connection.createStatement()) {

            statement.execute("CREATE TABLE test_table (id INT PRIMARY KEY, value VARCHAR(255))");
            int rowsAffected = statement.executeUpdate("INSERT INTO test_table (id, value) VALUES (1, 'test value')");

            assertThat(rowsAffected).isEqualTo(1);

            try (ResultSet resultSet = statement.executeQuery("SELECT value FROM test_table WHERE id = 1")) {
                assertThat(resultSet.next()).isTrue();
                assertThat(resultSet.getString("value")).isEqualTo("test value");
            }
        }
    }

    @Test
    void testKafka() {
        // ARRANGE
        var bootstrapUrl = kafka.getHost() + ":" + kafka.getMappedPort(9094);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapUrl);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testgroup");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // ACTION
        String topic = "test-topic";
        String message = "test message";

        try {
            produceMessageToKafka(topic, message);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // ASSERT
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("test-topic"));


            ConsumerRecord<String, String> record = consumer.poll(Duration.ofSeconds(10)).iterator().next();
            assertThat(record.value()).isEqualTo("test message");
        }
    }

    private void produceMessageToKafka(String topic, String message) throws ExecutionException, InterruptedException {
        var bootstrapUrl = kafka.getHost() + ":" + kafka.getMappedPort(9094);
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapUrl);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            producer.send(new ProducerRecord<>(topic, message)).get();
        }
    }

}
