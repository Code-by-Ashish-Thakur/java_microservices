package com.enrollment.emailworkerservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/api/email-worker/health")
    public Map<String, String> health() {
        log.info("[EMAIL-WORKER] Health check endpoint called");
        return Map.of(
                "service", "email-worker-service",
                "status", "UP",
                "queue", "email.queue"
        );
    }
}
