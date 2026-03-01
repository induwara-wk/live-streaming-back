package com.test.live_stream.lms.controller;

import com.test.live_stream.lms.service.VideoRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/livekit")
public class LiveKitController {

    @Autowired
    private VideoRoomService videoRoomService;

    @GetMapping("/token")
    public Map<String, String> getToken(@RequestParam String roomName, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        String token = videoRoomService.generateJoinToken(userId, roomName);
        return Map.of("token", token);
    }
}
