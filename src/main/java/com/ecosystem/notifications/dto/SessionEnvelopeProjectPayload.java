package com.ecosystem.notifications.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionEnvelopeProjectPayload implements SessionEnvelopePayload{
    private UUID projectId;
}
