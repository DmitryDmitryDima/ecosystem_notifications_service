package com.ecosystem.notifications.queue_events.observer_events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class ObserverEvent {

    private String type;
}
