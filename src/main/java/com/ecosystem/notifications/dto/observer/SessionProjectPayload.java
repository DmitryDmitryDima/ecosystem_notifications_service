package com.ecosystem.notifications.dto.observer;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionProjectPayload implements SessionPayload {
    private UUID projectId;

}
