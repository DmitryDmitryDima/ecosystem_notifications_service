package com.ecosystem.notifications.queue_events.external_events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEventContext extends EventContext {

    private  String username;
    private  UUID userUUID;



}
