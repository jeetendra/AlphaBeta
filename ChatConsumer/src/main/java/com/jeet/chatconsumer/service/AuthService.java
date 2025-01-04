package com.jeet.chatconsumer.service;

import com.jeet.chatconsumer.dto.LoginResponse;
import com.jeet.chatconsumer.dto.UserDto;
import com.jeet.chatconsumer.model.User;
import com.jeet.chatconsumer.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecretKey jwtSecretKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public Mono<LoginResponse> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    String token = generateToken(user);
                    return new LoginResponse(token, convertToDto(user));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getAvatarUrl(),
                user.getCreatedAt()
        );
    }

    public Mono<UserDto> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDto);
    }

    public Mono<UserDto> saveUser(User user) {
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user).map(this::convertToDto);
    }

    public Mono<UserDto> register(User user) {
        return findUserByEmail(user.getEmail())
                .log("register")
                .flatMap(existingUser -> Mono.<UserDto>error(new RuntimeException("Email already exists")))
                .switchIfEmpty(Mono.defer(() -> saveUser(user)));
    }

    private String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtExpiration, ChronoUnit.MILLIS);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(jwtSecretKey)
                .compact();
    }
}
