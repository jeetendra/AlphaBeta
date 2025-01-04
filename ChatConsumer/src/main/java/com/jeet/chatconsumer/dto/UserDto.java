package com.jeet.chatconsumer.dto;

import java.time.Instant;
import java.util.UUID;

public record UserDto(UUID id, String email, String username, String avatarUrl, Instant createdAt) {
}
