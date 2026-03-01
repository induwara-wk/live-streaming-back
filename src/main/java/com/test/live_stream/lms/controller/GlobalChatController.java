package com.test.live_stream.lms.controller;

import com.test.live_stream.lms.dto.ChatMessageRequest;
import com.test.live_stream.lms.service.GlobalChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class GlobalChatController {

    private final GlobalChatService chatService;

    public GlobalChatController(GlobalChatService chatService) {
        this.chatService = chatService;
    }

    // Send a message
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageRequest request, Authentication auth) {
        try {
            String userId = (String) auth.getPrincipal();
            return ResponseEntity.ok(chatService.sendMessage(userId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get conversation with a specific user
    @GetMapping("/conversation/{otherUserId}")
    public ResponseEntity<?> getConversation(@PathVariable String otherUserId, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(chatService.getConversation(userId, otherUserId));
    }

    // Get list of users I've chatted with
    @GetMapping("/partners")
    public ResponseEntity<?> getChatPartners(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(chatService.getChatPartners(userId));
    }

    // Search users to start a new conversation
    @GetMapping("/users/search")
    public ResponseEntity<?> searchUsers(@RequestParam String q, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(chatService.searchUsers(q, userId));
    }
}
