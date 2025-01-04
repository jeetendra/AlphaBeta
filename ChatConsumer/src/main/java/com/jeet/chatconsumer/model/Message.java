package com.jeet.chatconsumer.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Table(name = "messages")
public class Message {
    @Id
    private UUID id;
    private UUID chatId;
    private UUID senderId;
    private String content;
    private String mediaUrl;
    private Instant createdAt;
}
