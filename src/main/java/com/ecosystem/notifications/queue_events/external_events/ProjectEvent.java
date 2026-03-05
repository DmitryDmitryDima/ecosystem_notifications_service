package com.ecosystem.notifications.queue_events.external_events;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEvent extends QueueEvent {

    private ProjectEventContext context;

}
