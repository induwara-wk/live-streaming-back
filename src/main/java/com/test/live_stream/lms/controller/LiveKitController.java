package com.test.live_stream.lms.controller;

import com.test.live_stream.lms.model.LiveSession;
import com.test.live_stream.lms.model.User;
import com.test.live_stream.lms.repository.LiveSessionRepository;
import com.test.live_stream.lms.repository.UserRepository;
import com.test.live_stream.lms.service.VideoRoomService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/livekit")
public class LiveKitController {

    private final VideoRoomService videoRoomService;
    private final LiveSessionRepository sessionRepository;
    private final UserRepository userRepository;

    public LiveKitController(VideoRoomService videoRoomService,
            LiveSessionRepository sessionRepository,
            UserRepository userRepository) {
        this.videoRoomService = videoRoomService;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/token")
    public ResponseEntity<?> getToken(@RequestParam String sessionId, Authentication auth) {
        String userId = (String) auth.getPrincipal();

        // Get the session to determine room name and check creator
        LiveSession session = sessionRepository.findById(sessionId).orElse(null);
        if (session == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Session not found"));
        }

        // Get user for display name
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        // Room name = sessionId (unique per session)
        String roomName = session.getSessionId();
        String participantName = user.getFullName();
        boolean isModerator = session.getCreator().getUserId().equals(userId);

        String token = videoRoomService.generateJoinToken(userId, participantName, roomName, isModerator);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "roomName", roomName,
                "isModerator", String.valueOf(isModerator)));
    }
}
