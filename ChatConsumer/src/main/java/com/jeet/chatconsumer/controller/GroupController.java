package com.jeet.chatconsumer.controller;

import com.jeet.chatconsumer.dto.CreateGroupRequest;
import com.jeet.chatconsumer.model.Chat;
import com.jeet.chatconsumer.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public Mono<Chat> createGroup(@RequestBody CreateGroupRequest request) {
        return groupService.createGroup(request.getName(), request.getParticipantIds());
    }

    @PostMapping("/{chatId}/participants")
    public Mono<Void> addParticipants(
            @PathVariable UUID chatId,
            @RequestBody Set<UUID> participantIds) {
        return groupService.addParticipants(chatId, participantIds);
    }

    @DeleteMapping("/{chatId}/participants/{userId}")
    public Mono<Void> removeParticipant(
            @PathVariable UUID chatId,
            @PathVariable UUID userId) {
        return groupService.removeParticipant(chatId, userId);
    }
}
