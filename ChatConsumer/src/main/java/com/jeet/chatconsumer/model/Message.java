package com.jeet.chatconsumer.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
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
    @CreatedDate
    @Column("created_at")
    private Instant createdAt;
}
