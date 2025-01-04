package com.jeet.chatconsumer.repository;

import com.jeet.chatconsumer.model.Chat;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ChatRepository extends R2dbcRepository<Chat, String> {
}
