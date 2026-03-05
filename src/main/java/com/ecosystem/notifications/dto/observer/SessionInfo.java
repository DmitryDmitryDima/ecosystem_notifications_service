package com.ecosystem.notifications.dto.observer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionInfo {

    private String sessionId;



    private SecurityContext securityContext;




}
