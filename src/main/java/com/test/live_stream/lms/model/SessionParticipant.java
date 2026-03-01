package com.test.live_stream.lms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "session_participants")
public class SessionParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private LiveSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime joinTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomRole roleInRoom;

    @PrePersist
    protected void onCreate() {
        if (joinTime == null)
            joinTime = LocalDateTime.now();
    }

    public SessionParticipant() {
    }

    // Getters and Setters
    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public LiveSession getSession() {
        return session;
    }

    public void setSession(LiveSession session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }

    public RoomRole getRoleInRoom() {
        return roleInRoom;
    }

    public void setRoleInRoom(RoomRole roleInRoom) {
        this.roleInRoom = roleInRoom;
    }

    public enum RoomRole {
        Attendee, Moderator
    }
}
