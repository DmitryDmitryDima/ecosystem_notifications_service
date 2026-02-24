package com.ecosystem.notifications.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEventContext {
    private  Instant timestamp;
    private  String username;
    private  UUID userUUID;
    private UUID correlationId;

    // данное поле говорит - нужно ли дублирование ивента в публичный канал (пример - удаление проекта видно тому, кто смотрит на чужие проекты)
    private boolean opened;
}
