package com.ecosystem.notifications.queue_events.external_events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// если требуется персональная рассылка + какое то действие в notification слое (например - закрыть сессию)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmStrategy {
    private List<UUID> alarmList = new ArrayList<>();
    private AlarmAction action;

}
