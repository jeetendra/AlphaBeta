package com.jeet.chatconsumer.exception;

import com.jeet.chatconsumer.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorResponse> handleChatException(ChatException ex) { // Use ResponseEntity
        log.error("Chat Exception occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.badRequest().body(error); // Use ResponseEntity.body()
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) { // Use ResponseEntity
        log.error("Generic Exception occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse("An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // Use ResponseEntity.body()
    }
}

