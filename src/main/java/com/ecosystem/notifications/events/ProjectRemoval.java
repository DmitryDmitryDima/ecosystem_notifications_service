package com.ecosystem.notifications.events;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRemoval {
    private ProjectRemovalStatus status;
    private String message;
    private Long projectId;
    private String projectLanguage;

    private UserEventContext context;
}
