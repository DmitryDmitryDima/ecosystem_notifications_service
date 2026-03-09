package com.ecosystem.notifications.queue_events.external_events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class EventContext {

    // correlation id процесса - каждый процесс в системе должен иметь свой correlation id
    private UUID correlationId;

    private Instant timestamp;

    private AlarmStrategy alarmStrategy;

    private NotificationStrategy notificationStrategy;


}
