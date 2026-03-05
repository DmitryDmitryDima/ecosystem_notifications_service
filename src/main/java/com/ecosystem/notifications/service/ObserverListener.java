package com.ecosystem.notifications.service;

import com.ecosystem.notifications.queue_events.observer_events.ObserverEvent;
import com.ecosystem.notifications.queue_events.observer_events.ProjectObserverEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class ObserverListener {
    @Autowired
    private SimpMessagingTemplate notifier;
    @Autowired
    private ObjectMapper mapper;

    @EventListener
    public void processObserverEvent(ObserverEvent observerEvent){
        switch (observerEvent){
            case ProjectObserverEvent projectObserverEvent->{
                notifier.convertAndSend("/projects/"+projectObserverEvent.getContext().getProjectId(),
                        mapper.writeValueAsString(projectObserverEvent));
            }
            default -> {}
        }

    }
}
