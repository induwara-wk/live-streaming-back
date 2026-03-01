package com.test.live_stream.lms.dto;

import java.time.LocalDateTime;

public class SessionRequest {
    private String title;
    private String description;
    private LocalDateTime scheduledStartTime; // null for immediate sessions

    public SessionRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getScheduledStartTime() {
        return scheduledStartTime;
    }

    public void setScheduledStartTime(LocalDateTime scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }
}
