package com.ecosystem.notifications.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsernameUUIDPair {
    private String username;
    private UUID uuid;

}
