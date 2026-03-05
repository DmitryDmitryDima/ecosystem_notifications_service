package com.ecosystem.notifications.dto.observer;

import lombok.*;

import java.util.Map;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SecurityContext {
    private UUID uuid;

    private String username;

    private String role;






    public static SecurityContext generateContext(Map<String, String> requestHeaders){




        return SecurityContext
                .builder()
                .role(requestHeaders.get("role"))
                .uuid(UUID.fromString(requestHeaders.get("uuid")))
                .username(requestHeaders.get("username"))
                .build();
    }
}
