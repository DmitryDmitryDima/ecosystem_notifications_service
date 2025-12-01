package com.ecosystem.notifications.service;


import com.ecosystem.notifications.events.ProjectRemoval;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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



    @RabbitListener(queues = {"${users.activity_events.queue.name}"})
    public void receiveUserCreationEvent(@Payload String payload,
                                         @Header("event_type") String eventType) throws Exception {


        if (eventType.equals(PROJECT_REMOVAL_EVENT)){
            ProjectRemoval removalEvent = objectMapper.readValue(payload, ProjectRemoval.class);
            System.out.println(removalEvent);
            notifier.convertAndSend("/users/activity/"+removalEvent.getContext().getUserUUID(), removalEvent.getMessage());


        }











    }



}
