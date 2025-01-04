package com.jeet.chatconsumer.dto;

public record UserRegisterRequest(String email, String password, String username, String avatarUrl) {
}
