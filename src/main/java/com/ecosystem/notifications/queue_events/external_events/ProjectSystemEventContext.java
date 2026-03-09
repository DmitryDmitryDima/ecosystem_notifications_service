package com.ecosystem.notifications.queue_events.external_events;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSystemEventContext extends EventContext {

    //private Instant timestamp;

    private UUID projectId;

    // название системного процесса (опционально)
    private String origin;

    // correlation id процесса - каждый процесс в системе должен иметь свой correlation id
    //private UUID correlationId;


}
