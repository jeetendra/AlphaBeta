package com.jeet.chatconsumer.dto;

import com.jeet.chatconsumer.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserDto user;
}