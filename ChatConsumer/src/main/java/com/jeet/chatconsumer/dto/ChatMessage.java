package com.jeet.chatconsumer.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ChatMessage {
    private UUID chatId;
    private String content;
    private String mediaUrl;
}
