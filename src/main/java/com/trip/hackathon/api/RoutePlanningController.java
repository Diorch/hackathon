package com.trip.hackathon.api;

import com.trip.hackathon.engine.RoutingEngine;
import com.trip.hackathon.engine.model.PlannedRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController("rp")
public class RoutePlanningController {

    @Autowired
    private RoutingEngine saEngine;

    @GetMapping("/run/")
    public ResponseEntity<PlannedRoute> run(LocalDateTime sdt, LocalDateTime edt) {
        saEngine.doWork();
        return ResponseEntity.ok().build();
    }
}
