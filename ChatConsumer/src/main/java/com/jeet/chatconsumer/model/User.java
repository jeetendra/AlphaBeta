package com.jeet.chatconsumer.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Table("users")
public class User {
    @Id
    private UUID id;
    private String email;
    private String username;
    private String password;
    private String avatarUrl;
    private Instant createdAt;
}