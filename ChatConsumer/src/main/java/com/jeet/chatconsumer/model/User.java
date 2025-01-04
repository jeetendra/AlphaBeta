package com.jeet.chatconsumer.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Table("users")
public class User {
    @Id
    private UUID id;
    private String email;
    private String username;
    private String password;
    private String avatarUrl;
    @CreatedDate
    @Column("created_at")
    private Instant createdAt;
}