package com.ecosystem.notifications.service;



import com.ecosystem.notifications.events.UserEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class RabbitMQListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpMessagingTemplate notifier;

    private static final String PROJECT_REMOVAL_EVENT = "java_project_removal";
    private static final String PROJECT_CREATION_EVENT ="java_project_creation";



    @RabbitListener(queues = {"${users.activity_events.queue.name}"})
    public void receiveUserCreationEvent(@Payload String payload,
                                         @Header("event_type") String eventType) throws Exception {


        if (eventType.equals(PROJECT_REMOVAL_EVENT)||eventType.equals(PROJECT_CREATION_EVENT)){
            UserEvent event = objectMapper.readValue(payload, UserEvent.class);
            notifier.convertAndSend("/users/activity/"+event.getContext().getUserUUID(), payload);




        }











    }



}
