package com.flight.management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/keep-alive")
public class RenderKeepAliveController {

    @GetMapping
    public ResponseEntity<String> keepAlive() {
        return ResponseEntity.ok("Service is active");
    }
}
