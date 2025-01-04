package com.jeet.chatconsumer.service;

import com.jeet.chatconsumer.model.Chat;
import com.jeet.chatconsumer.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final ChatRepository chatRepository;

    public Mono<Chat> createGroup(String name, Set<UUID> participantIds) {
        Chat chat = new Chat();
        chat.setName(name);
        chat.setGroup(true);

        return chatRepository.save(chat)
                .flatMap(savedChat -> addParticipants(savedChat.getId(), participantIds)
                        .thenReturn(savedChat));
    }

    public Mono<Void> addParticipants(UUID chatId, Set<UUID> participantIds) {
        return Mono.when(
                participantIds.stream()
                        .map(userId -> chatRepository.addParticipant(chatId, userId))
                        .toList()
        );
    }

    public Mono<Void> removeParticipant(UUID chatId, UUID userId) {
        return chatRepository.removeParticipant(chatId, userId);
    }
}
