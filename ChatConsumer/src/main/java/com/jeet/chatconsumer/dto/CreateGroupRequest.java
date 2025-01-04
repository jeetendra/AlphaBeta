package com.jeet.chatconsumer.dto;

import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
public class CreateGroupRequest {
    private String name;
    private Set<UUID> participantIds;
}
