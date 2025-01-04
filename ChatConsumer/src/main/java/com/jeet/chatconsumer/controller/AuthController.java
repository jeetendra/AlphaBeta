package com.jeet.chatconsumer.controller;


import com.jeet.chatconsumer.dto.LoginRequest;
import com.jeet.chatconsumer.dto.LoginResponse;
import com.jeet.chatconsumer.dto.UserDto;
import com.jeet.chatconsumer.dto.UserRegisterRequest;
import com.jeet.chatconsumer.model.User;
import com.jeet.chatconsumer.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public Mono<LoginResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserRegisterRequest user) {
        return authService.register(User.builder().email(user.email())
                .username(user.username())
                .password(user.password())
                .avatarUrl(user.avatarUrl())
                .build());
    }
}
