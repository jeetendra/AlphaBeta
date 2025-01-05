package com.jeet.chatconsumer.exception;

import com.jeet.chatconsumer.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChatException.class)
    public Mono<ServerResponse> handleChatException(ChatException ex) {
        log.error("Chat Exception occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(error);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ServerResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse("An unexpected error occurred");
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(error);
    }
}

