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
public class ProjectSystemEventContext {

    private Instant timestamp;

    private UUID projectId;

    // название системного процесса (опционально)
    private String origin;

    // correlation id процесса - каждый процесс в системе должен иметь свой correlation id
    private UUID correlationId;

    // участники проекта - опционально для случаев, где требуется персональная рассылка участникам проекта в приватный канал
    // если список не пуст - значит рассылка нужна
    private List<UUID> participants;
}
