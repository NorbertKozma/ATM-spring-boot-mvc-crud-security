package com.nor2code.springboot.thymeleafdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class HealthCheckController {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    private final Instant startupTime;

    public HealthCheckController() {
        this.startupTime = Instant.now();
    }

    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        logger.info("Health check requested at {}", Instant.now());
        String status = "Application is healthy. Startup time: " + startupTime.toString();
        return ResponseEntity.ok(status);
    }
}
