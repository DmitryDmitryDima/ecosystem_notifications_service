package com.ecosystem.notifications.api;


import com.ecosystem.notifications.dto.api.UsernameUUIDPair;
import com.ecosystem.notifications.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class ObserverAPI {

    @Autowired
    private ObservationService observationService;


    //
    @GetMapping("/getProjectSubscriptions/{uuid}")
    public ResponseEntity<Set<UsernameUUIDPair>> getProjectSubscriptions(@PathVariable("uuid") UUID projectId){

        return ResponseEntity.ok(observationService.getAllProjectSubscriptions(projectId));
    }





}
