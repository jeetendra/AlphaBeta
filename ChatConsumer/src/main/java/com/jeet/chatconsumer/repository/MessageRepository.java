package com.jeet.chatconsumer.repository;

import com.jeet.chatconsumer.model.Message;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface MessageRepository extends R2dbcRepository<Message, UUID> {

    Flux<Message> findByChatIdOrderByCreatedAtDesc(UUID chatId);

}
