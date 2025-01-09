package com.jeet.chatconsumer.controller;


import com.jeet.chatconsumer.dto.ChatMessage;
import com.jeet.chatconsumer.model.Chat;
import com.jeet.chatconsumer.model.Message;
import com.jeet.chatconsumer.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public Flux<Chat> getUserChats(@RequestHeader("X-User-Id") UUID userId) {
        return chatService.getUserChats(userId);
    }

    @GetMapping("/{chatId}/messages")
    public Flux<Message> getChatMessages(@PathVariable UUID chatId) {
        return chatService.getChatMessages(chatId);
    }

    @PostMapping("/{chatId}/messages")
    public Mono<Message> sendMessage(@PathVariable UUID chatId, @RequestBody ChatMessage message, @RequestHeader("X-User-Id") UUID senderId) {
        return chatService.sendMessage(chatId, message.getContent(), message.getMediaUrl(), senderId);
    }

    @GetMapping("/{chatId}/participants")
    public Flux<UUID> getChatParticipants(@PathVariable UUID chatId) {
        return chatService.getChatParticipants(chatId);
    }
}
