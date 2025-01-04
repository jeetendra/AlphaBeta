package com.jeet.chatconsumer.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Table("chats")
public class Chat {
    @Id
    private UUID id;
    private String name;
    private boolean isGroup;
    private Instant createdAt;
}
