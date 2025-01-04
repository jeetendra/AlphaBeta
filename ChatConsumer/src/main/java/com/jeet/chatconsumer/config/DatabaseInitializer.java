package com.jeet.chatconsumer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableR2dbcAuditing
public class DatabaseInitializer {


    @Bean
    CommandLineRunner initializeDatabase(DatabaseClient client) {
        return args -> initializeSchema(client).subscribe();
    }

    private Mono<Void> initializeSchema(DatabaseClient client) {
        return createExtension(client)
                .then(createUsersTable(client))
                .then(createChatsTable(client))
                .then(createMessagesTable(client))
                .then(createChatParticipantsTable(client))
                .then(); // Returns Mono<Void>
    }

    private Mono<Long> createExtension(DatabaseClient client) {
        return client.sql("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";")
                .fetch()
                .rowsUpdated()
                .doOnSuccess(count -> log.info("UUID-OSSP extension created (if not already existed)"))
                .onErrorResume(e -> {
                    log.error("Error creating extension: {}", e.getMessage());
                    return Mono.empty(); // Continue even if extension creation fails
                });
    }

    private Mono<Long> createUsersTable(DatabaseClient client) {
        String sql = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS users (\n")
                .append("    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),\n")
                .append("    email VARCHAR(255) UNIQUE NOT NULL,\n")
                .append("    username VARCHAR(255) NOT NULL,\n")
                .append("    password VARCHAR(255) NOT NULL,\n")
                .append("    avatar_url VARCHAR(255),\n")
                .append("    created_at TIMESTAMP WITH TIME ZONE\n")
                .append(");")
                .toString();

        return executeSql(client, sql, "users");
    }

    private Mono<Long> createChatsTable(DatabaseClient client) {
        String sql = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS chats (\n")
                .append("    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),\n")
                .append("    name VARCHAR(255),\n")
                .append("    is_group BOOLEAN NOT NULL DEFAULT FALSE,\n")
                .append("    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP\n")
                .append(");")
                .toString();
        return executeSql(client, sql, "chats");

    }

    private Mono<Long> createMessagesTable(DatabaseClient client) {
        String sql = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS messages (\n")
                .append("    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),\n")
                .append("    chat_id UUID NOT NULL,\n")
                .append("    sender_id UUID NOT NULL,\n")
                .append("    content TEXT,\n")
                .append("    media_url VARCHAR(255),\n")
                .append("    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,\n")
                .append("    FOREIGN KEY (chat_id) REFERENCES chats(id),\n")
                .append("    FOREIGN KEY (sender_id) REFERENCES users(id)\n")
                .append(");")
                .toString();

        return executeSql(client, sql, "messages");
    }

    private Mono<Long> createChatParticipantsTable(DatabaseClient client) {
        String sql = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS chat_participants (\n")
                .append("    chat_id UUID NOT NULL,\n")
                .append("    user_id UUID NOT NULL,\n")
                .append("    PRIMARY KEY (chat_id, user_id), -- Composite primary key\n")
                .append("    FOREIGN KEY (chat_id) REFERENCES chats(id),\n")
                .append("    FOREIGN KEY (user_id) REFERENCES users(id)\n")
                .append(");")
                .toString();
        return executeSql(client, sql, "chat_participants");
    }

    private Mono<Long> executeSql(DatabaseClient client, String sql, String tableName) {
        return client.sql(sql)
                .fetch()
                .rowsUpdated()
                .doOnSuccess(count -> log.info("Table {} created (if not already existed)", tableName))
                .onErrorResume(e -> {
                    log.error("Error creating table {}: {}", tableName, e.getMessage());
                    return Mono.empty();
                });
    }

}
