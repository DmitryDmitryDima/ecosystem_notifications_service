package com.ecosystem.notifications.queue_events.observer_events;

import com.ecosystem.notifications.queue_events.external_events.ProjectEventContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectObserverEvent extends ObserverEvent {

    ProjectEventContext context;
}
