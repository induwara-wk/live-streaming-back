package com.test.live_stream.lms.service;

import com.test.live_stream.lms.dto.SessionRequest;
import com.test.live_stream.lms.dto.SessionResponse;
import com.test.live_stream.lms.model.LiveSession;
import com.test.live_stream.lms.model.User;
import com.test.live_stream.lms.repository.LiveSessionRepository;
import com.test.live_stream.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {

    private final LiveSessionRepository sessionRepository;
    private final UserRepository userRepository;

    public SessionService(LiveSessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public SessionResponse createSession(SessionRequest request, String creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LiveSession session = new LiveSession();
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setCreator(creator);

        // If no scheduled time → immediate/live session
        if (request.getScheduledStartTime() == null) {
            session.setStatus(LiveSession.SessionStatus.Live);
            session.setActualStartTime(LocalDateTime.now());
        } else {
            session.setScheduledStartTime(request.getScheduledStartTime());
            session.setStatus(LiveSession.SessionStatus.Scheduled);
        }

        sessionRepository.save(session);
        return toResponse(session);
    }

    public List<SessionResponse> getActiveSessions() {
        List<LiveSession.SessionStatus> active = List.of(
                LiveSession.SessionStatus.Live,
                LiveSession.SessionStatus.Scheduled);
        return sessionRepository.findByStatusInOrderByCreatedAtDesc(active)
                .stream().map(this::toResponse).toList();
    }

    public SessionResponse getSession(String sessionId) {
        LiveSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return toResponse(session);
    }

    public SessionResponse updateSession(String sessionId, SessionRequest request, String userId) {
        LiveSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getCreator().getUserId().equals(userId)) {
            throw new RuntimeException("Only the session creator can edit this session");
        }

        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        if (request.getScheduledStartTime() != null) {
            session.setScheduledStartTime(request.getScheduledStartTime());
        }

        sessionRepository.save(session);
        return toResponse(session);
    }

    public void deleteSession(String sessionId, String userId) {
        LiveSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getCreator().getUserId().equals(userId)) {
            throw new RuntimeException("Only the session creator can delete this session");
        }

        sessionRepository.delete(session);
    }

    public SessionResponse endSession(String sessionId, String userId) {
        LiveSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getCreator().getUserId().equals(userId)) {
            throw new RuntimeException("Only the session creator can end this session");
        }

        session.setStatus(LiveSession.SessionStatus.Completed);
        session.setActualEndTime(LocalDateTime.now());
        sessionRepository.save(session);
        return toResponse(session);
    }

    private SessionResponse toResponse(LiveSession session) {
        return new SessionResponse(
                session.getSessionId(),
                session.getTitle(),
                session.getDescription(),
                session.getScheduledStartTime(),
                session.getActualStartTime(),
                session.getActualEndTime(),
                session.getStatus().name(),
                session.getCreator().getUserId(),
                session.getCreator().getFullName(),
                session.getCreatedAt());
    }
}
