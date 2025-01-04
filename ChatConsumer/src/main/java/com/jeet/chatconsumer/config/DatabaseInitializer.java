package com.jeet.chatconsumer.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@Configuration
@EnableR2dbcAuditing
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initializeDatabase(DatabaseClient client) {
        return args -> {
            client.sql("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";")
                    .fetch()
                    .rowsUpdated()
                    .then( // Use then() to chain operations sequentially
                            client.sql("CREATE TABLE IF NOT EXISTS users (\n" +
                                            "    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),\n" +
                                            "    email VARCHAR(255) UNIQUE NOT NULL,\n" +
                                            "    username VARCHAR(255) NOT NULL,\n" +
                                            "    password VARCHAR(255) NOT NULL,\n" +
                                            "    avatar_url VARCHAR(255),\n" +
                                            "    created_at TIMESTAMP WITHOUT TIME ZONE\n" +
                                            ")")
                                    .fetch()
                                    .rowsUpdated()
                    )
                    .subscribe();
        };
    }

}
