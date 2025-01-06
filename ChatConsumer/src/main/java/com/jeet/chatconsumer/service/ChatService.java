package com.jeet.chatconsumer.service;

import com.jeet.chatconsumer.model.Chat;
import com.jeet.chatconsumer.model.Message;
import com.jeet.chatconsumer.repository.ChatRepository;
import com.jeet.chatconsumer.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public Flux<Chat> getUserChats(UUID userId) {
        return chatRepository.findByParticipantsContaining(userId);
    }

    public Flux<Message> getChatMessages(UUID chatId) {
        return messageRepository.findByChatIdOrderByCreatedAtDesc(chatId);
    }

    public Flux<UUID> getChatParticipants(UUID chatId) {
        return chatRepository.findParticipantsByChatId(chatId);
    }

    public Mono<Message> sendMessage(UUID chatId, String content, String mediaUrl, UUID senderId) {
        Message message = new Message();
        message.setChatId(chatId);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setMediaUrl(mediaUrl);

        return messageRepository.save(message);
    }
}
