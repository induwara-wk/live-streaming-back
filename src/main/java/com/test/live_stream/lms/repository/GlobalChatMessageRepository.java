package com.test.live_stream.lms.repository;

import com.test.live_stream.lms.model.GlobalChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GlobalChatMessageRepository extends JpaRepository<GlobalChatMessage, String> {

    // Get conversation between two users (bidirectional)
    @Query("SELECT m FROM GlobalChatMessage m WHERE " +
            "(m.sender.userId = :userA AND m.receiver.userId = :userB) OR " +
            "(m.sender.userId = :userB AND m.receiver.userId = :userA) " +
            "ORDER BY m.timestamp ASC")
    List<GlobalChatMessage> findConversation(@Param("userA") String userA, @Param("userB") String userB);

    // Get all users who have had a conversation with a given user (for chat list)
    @Query("SELECT DISTINCT CASE WHEN m.sender.userId = :userId THEN m.receiver ELSE m.sender END " +
            "FROM GlobalChatMessage m WHERE m.sender.userId = :userId OR m.receiver.userId = :userId")
    List<com.test.live_stream.lms.model.User> findChatPartners(@Param("userId") String userId);
}
