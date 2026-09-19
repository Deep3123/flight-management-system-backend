package com.flight.management.controller;

import com.flight.management.proxy.ChatRequest;
import com.flight.management.proxy.ChatResponse;
import com.flight.management.service.impl.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String response = chatbotService.chat(request.getMessage());
        return ResponseEntity.ok(new ChatResponse(response));
    }
}
