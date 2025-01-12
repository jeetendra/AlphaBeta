package com.jeet.schedular.handler;

import com.jeet.schedular.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class UserHandler {
    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(User.class)
                .flatMap(user -> {
                    log.info("User {} received", user);
                    return ServerResponse.ok().bodyValue("User "+ user.getName() + " added successfully");
                });
    }
}
