package com.test.live_stream.lms.repository;

import com.test.live_stream.lms.model.LiveSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LiveSessionRepository extends JpaRepository<LiveSession, String> {
    List<LiveSession> findByStatusInOrderByCreatedAtDesc(List<LiveSession.SessionStatus> statuses);

    List<LiveSession> findByCreator_UserIdOrderByCreatedAtDesc(String creatorId);
}
