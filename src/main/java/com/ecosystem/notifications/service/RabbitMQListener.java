package com.ecosystem.notifications.service;



import com.ecosystem.notifications.events.ProjectEvent;
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



    private static final String PROJECT_FILE_SAVE_EVENT = "java_project_file_save";


    @RabbitListener(queues = {"${users.activity_events.queue.name}"})
    public void receiveUserActivityEvent(@Payload String payload,
                                         @Header("event_type") String eventType) throws Exception {


        System.out.println(payload);
        if (eventType.equals(PROJECT_REMOVAL_EVENT)||eventType.equals(PROJECT_CREATION_EVENT)){
            UserEvent event = objectMapper.readValue(payload, UserEvent.class);
            notifier.convertAndSend("/users/activity/"+event.getContext().getUserUUID(), payload);




        }


    }

    @RabbitListener(queues = {"${users.projects_events.queue.name}"})
    public void receiveProjectsEvent(@Payload String payload, @Header("event_type") String eventType) throws Exception{

        System.out.println(payload);

        // данное событие предназначено только для комнаты проекта, персональная рассылка не требуется
        if (eventType.equals(PROJECT_FILE_SAVE_EVENT)){
            ProjectEvent event = objectMapper.readValue(payload,  ProjectEvent.class);
            notifier.convertAndSend("/projects/java/"+event.getContext().getProjectId(), payload);
        }
    }





}
