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
public class ProjectEventContext extends EventContext {

    // автор ивента
    //private Instant timestamp;
    private  String username;
    private UUID userUUID;
    //private UUID correlationId;
    private UUID renderId;

    //private AlarmStrategy alarmStrategy;

    // todo participant role - роль участника - к примеру author, project admin (not always author only)

    // информация по проекту
    private UUID projectId;







}
