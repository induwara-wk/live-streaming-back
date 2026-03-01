package com.test.live_stream.lms.controller;

import com.test.live_stream.lms.service.VideoRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/livekit")
@CrossOrigin(origins = "http://localhost:5173") // Vite's default port
public class LiveKitController {

    @Autowired
    private VideoRoomService videoRoomService;

    @GetMapping("/token")
    public Map<String, String> getToken(@RequestParam String participantName, @RequestParam String roomName) {
        String token = videoRoomService.generateJoinToken(participantName, roomName);
        return Map.of("token", token);
    }
}
