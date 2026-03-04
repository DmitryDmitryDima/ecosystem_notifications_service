package com.ecosystem.notifications.service;


import com.ecosystem.notifications.dto.SessionEnvelope;
import com.ecosystem.notifications.dto.SessionEnvelopePayload;
import com.ecosystem.notifications.dto.SessionEnvelopeProjectPayload;
import com.ecosystem.notifications.dto.SessionInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Service
public class ObservationService {

    private final ConcurrentHashMap<UUID, ConcurrentMap<String, SessionInfo>> projectSessions  =new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, SessionEnvelope> allSessions = new ConcurrentHashMap<>();

    public void registerSession(WebSocketSession session){
        allSessions.put(session.getId(), new SessionEnvelope(session));
    }

    // очищаем также все связанные с сессией зависимости. если они есть
    public void unregisterSession(String sessionId){
        SessionEnvelope envelope = allSessions.remove(sessionId);

        if (envelope==null) return;

        SessionEnvelopePayload payload = envelope.getPayload();

        if (payload == null) return;

        if (payload instanceof SessionEnvelopeProjectPayload projectPayload){
            removeProjectSessionInfo(projectPayload.getProjectId(), sessionId);
        }





    }


    public void addProjectSessionInfo(UUID projectId, SessionInfo sessionInfo){

        allSessions.computeIfPresent(sessionInfo.getSessionId(), (id, envelope)->{
            envelope.setPayload(new SessionEnvelopeProjectPayload(projectId));
            projectSessions.compute(projectId, (uuid, map)->{
                if (map==null){
                    map = new ConcurrentHashMap<>();


                }
                map.put(sessionInfo.getSessionId(), sessionInfo);
                return map;
            });
            return envelope;
        });

    }

    // удаляем информацию об очереди в проекте
    public void removeProjectSessionInfo(UUID projectId, String sessionId){

        projectSessions.computeIfPresent(projectId, (uuid, map)->{

            map.remove(sessionId);
            // если value null, это автоматически означает удаление ключа uuid из потокобезопасной очереди
            if (map.isEmpty()){
                return null;
            }

            return map;
        });
    }


    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void testStats(){
        System.out.println("Количество активных сессий: "+allSessions.size());
        System.out.println("Количество активных проектов: "+projectSessions.size());
    }






}
