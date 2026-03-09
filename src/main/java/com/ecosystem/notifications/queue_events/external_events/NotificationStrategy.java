package com.ecosystem.notifications.queue_events.external_events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationStrategy {

    // персональное сообщение в приватный канал
    private List<UUID> privateChannel;

    //  персональное сообщение в публичный канал
    private List<UUID> publicChannel;


}
