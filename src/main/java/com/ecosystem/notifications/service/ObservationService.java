package com.ecosystem.notifications.service;


import com.ecosystem.notifications.dto.observer.SessionEnvelope;
import com.ecosystem.notifications.dto.observer.SessionEnvelopePayload;
import com.ecosystem.notifications.dto.observer.SessionEnvelopeProjectPayload;
import com.ecosystem.notifications.dto.observer.SessionInfo;
import com.ecosystem.notifications.dto.api.UsernameUUIDPair;
import com.ecosystem.notifications.queue_events.external_events.ProjectEventContext;
import com.ecosystem.notifications.queue_events.observer_events.ObserverEventType;
import com.ecosystem.notifications.queue_events.observer_events.ProjectObserverEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Service
public class ObservationService {


    @Autowired
    public ApplicationEventPublisher publisher;



    private final ConcurrentHashMap<UUID, ConcurrentMap<String, SessionInfo>> projectSessions  =new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, SessionEnvelope> allSessions = new ConcurrentHashMap<>();

    public void registerSession(WebSocketSession session){
        allSessions.put(session.getId(), new SessionEnvelope(session));
    }

    // очищаем также все связанные с сессией зависимости. если они есть
    public void unregisterSession(String sessionId){
        SessionEnvelope envelope = allSessions.remove(sessionId);

        if (envelope==null) return;

        for (SessionEnvelopePayload payload:envelope.getPayload()){
            if (payload instanceof SessionEnvelopeProjectPayload projectPayload){
                removeProjectSessionInfo(projectPayload.getProjectId(), sessionId);
            }
        }

    }


    public void addProjectSessionInfo(UUID projectId, SessionInfo sessionInfo){
        System.out.println("добавляем подписку на комнату проекта");
        /*
        allSessions.computeIfPresent(sessionInfo.getSessionId(), (id, envelope)->{
            System.out.println("добавляем подписку на комнату проекта");
            envelope.getPayload().add(new SessionEnvelopeProjectPayload(projectId));
            projectSessions.compute(projectId, (uuid, map)->{
                if (map==null){
                    map = new ConcurrentHashMap<>();


                }

                // одна сессия может подписана только один раз на один и тот же проект
                map.put(sessionInfo.getSessionId(), sessionInfo);
                return map;
            });
            return envelope;
        });

         */

        allSessions.computeIfPresent(sessionInfo.getSessionId(), (id, envelope)->{
            System.out.println("добавляем подписку на комнату проекта");
            envelope.getPayload().add(new SessionEnvelopeProjectPayload(projectId));
            return envelope;
        });

        projectSessions.compute(projectId, (uuid, map)->{
            if (map==null){
                map = new ConcurrentHashMap<>();


            }

            // одна сессия может подписана только один раз на один и тот же проект
            map.put(sessionInfo.getSessionId(), sessionInfo);
            return map;
        });

        // публикуем ивент
        ProjectObserverEvent projectObserverEvent = new ProjectObserverEvent();
        projectObserverEvent.setType(ObserverEventType.PROJECT_SUB.getValue());
        ProjectEventContext context = new ProjectEventContext();
        context.setUsername(sessionInfo.getSecurityContext().getUsername());
        context.setUserUUID(sessionInfo.getSecurityContext().getUuid());
        context.setProjectId(projectId);
        projectObserverEvent.setContext(context);
        publisher.publishEvent(projectObserverEvent);

    }

    // удаляем информацию об очереди в проекте
    public void removeProjectSessionInfo(UUID projectId, String sessionId){

        projectSessions.computeIfPresent(projectId, (uuid, map)->{

            SessionInfo removed =map.remove(sessionId);
            // если value null, это автоматически означает удаление ключа uuid из потокобезопасной очереди
            if (map.isEmpty()){
                return null;
            }
            ProjectObserverEvent projectObserverEvent = new ProjectObserverEvent();
            projectObserverEvent.setType(ObserverEventType.PROJECT_UNSUB.getValue());
            ProjectEventContext context = new ProjectEventContext();
            context.setUsername(removed.getSecurityContext().getUsername());
            context.setUserUUID(removed.getSecurityContext().getUuid());
            context.setProjectId(projectId);
            projectObserverEvent.setContext(context);
            publisher.publishEvent(projectObserverEvent);

            return map;
        });





    }

    public Set<UsernameUUIDPair> getProjectSubscriptions(UUID projectUUID){
        ConcurrentMap<String, SessionInfo> sessions = projectSessions.get(projectUUID);
        Set<UsernameUUIDPair> answer = new HashSet<>();
        if (sessions==null) return answer;

        sessions.forEach((sessionId, info)->{
            UsernameUUIDPair pair = new UsernameUUIDPair(info.getSecurityContext().getUsername(), info.getSecurityContext().getUuid());
            System.out.println("pair detected "+pair);
            answer.add(pair);
        });

        return answer;
    }


    @Scheduled(fixedDelay = 15, timeUnit = TimeUnit.SECONDS)
    public void testStats(){

        /*
        System.out.println("Количество активных сессий: "+allSessions.size());
        System.out.println("Количество активных проектов: "+projectSessions.size());

        projectSessions.forEach((k,v)->{
            System.out.println("В проекте "+k);
            v.forEach((sessionId, sessionInfo )->{
                System.out.println("Обнаружена подписка со стороны "+sessionInfo.getSecurityContext().getUsername());
            });
        });




        System.out.println(getProjectSubscriptions(UUID.fromString("a29722f5-1341-45f8-bf05-991fa0dcd12c")));

         */
    }






}
