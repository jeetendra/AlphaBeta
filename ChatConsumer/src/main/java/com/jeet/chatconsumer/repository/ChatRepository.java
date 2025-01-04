package com.jeet.chatconsumer.repository;

import com.jeet.chatconsumer.model.Chat;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ChatRepository extends R2dbcRepository<Chat, UUID> {

    @Query("SELECT c.* FROM chats c JOIN chat_participants cp ON c.id = cp.chat_id WHERE cp.user_id = :userId")
    Flux<Chat> findByParticipantsContaining(UUID userId);

    @Query("SELECT user_id FROM chat_participants WHERE chat_id = :chatId")
    Flux<UUID> findParticipantsByChatId(UUID chatId);

    @Modifying
    @Query("INSERT INTO chat_participants (chat_id, user_id) VALUES (:chatId, :userId)")
    Mono<Void> addParticipant(UUID chatId, UUID userId);

    @Modifying
    @Query("DELETE FROM chat_participants WHERE chat_id = :chatId AND user_id = :userId")
    Mono<Void> removeParticipant(UUID chatId, UUID userId);
}
