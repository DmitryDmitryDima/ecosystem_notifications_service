package com.ecosystem.notifications.dto.observer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionPersonalSubPayload implements SessionPayload {

    // подписка на публичный или приватный персональный канал
    private boolean opened;

    private UUID target;

}
