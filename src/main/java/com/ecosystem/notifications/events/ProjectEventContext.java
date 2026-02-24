package com.ecosystem.notifications.events;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEventContext {

    // автор ивента
    private Instant timestamp;
    private  String username;
    private UUID userUUID;
    private UUID correlationId;
    private UUID renderId;

    // todo participant role - роль участника - к примеру author, project admin (not always author only)

    // информация по проекту
    private UUID projectId;

    // все остальные, кроме автора. Автор при этом тоже получает событие
    // при этом важно отметить, что данное поле не всегда используется
    // Если поле заполнено отправителем - это означает, что персональная пересылка необходима (в приватный канал)
    private List<UUID> participants;





}
