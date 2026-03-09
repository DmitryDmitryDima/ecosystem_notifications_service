package com.ecosystem.notifications.queue_events.external_events;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AlarmStrategy alarmStrategy;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private NotificationStrategy notificationStrategy;


}
