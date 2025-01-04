package com.jeet.chatconsumer.repository;

import com.jeet.chatconsumer.model.Message;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface MessageRepository extends R2dbcRepository<Message, String> {
}
