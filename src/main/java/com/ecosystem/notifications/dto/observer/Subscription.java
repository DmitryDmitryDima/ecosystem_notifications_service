package com.ecosystem.notifications.dto.observer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    private String sessionId;

    private UUID userUUID;
    private String username;

    private SessionPayload sessionPayload;

    public Subscription(String sessionId, SessionPayload payload){
        this.sessionId = sessionId;
        this.sessionPayload = payload;
    }
}
