package com.test.live_stream.lms.dto;

public class ChatMessageRequest {
    private String receiverId;
    private String messageText;

    public ChatMessageRequest() {
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
