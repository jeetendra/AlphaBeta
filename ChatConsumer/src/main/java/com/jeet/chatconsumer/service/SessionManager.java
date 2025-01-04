package com.jeet.chatconsumer.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final Map<UUID, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addSession(UUID userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    public void removeSession(UUID userId) {
        sessions.remove(userId);
    }

    public WebSocketSession getSession(UUID userId) {
        return sessions.get(userId);
    }

    public boolean hasSession(UUID userId) {
        return sessions.containsKey(userId);
    }
}
