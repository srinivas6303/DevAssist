package com.srinivas.devassist.controller;

import com.srinivas.devassist.dto.ChatRequest;
import com.srinivas.devassist.dto.ChatResponse;
import com.srinivas.devassist.model.ChatMessage;
import com.srinivas.devassist.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public String chat(Authentication authentication,
                       @RequestBody ChatRequest request) {

        String username = authentication.getName(); // 🔥 from JWT

        return chatService.getResponse(username, request.getMessage());
    }

    @GetMapping("/history")
    public List<ChatResponse> getHistory(Authentication authentication) {

        String username = authentication.getName(); // from JWT

        return chatService.getUserHistory(username);
    }
}