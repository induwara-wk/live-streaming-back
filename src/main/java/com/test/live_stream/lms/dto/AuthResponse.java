package com.test.live_stream.lms.dto;

public class AuthResponse {
    private String token;
    private String userId;
    private String fullName;
    private String email;
    private String userType;

    public AuthResponse() {
    }

    public AuthResponse(String token, String userId, String fullName, String email, String userType) {
        this.token = token;
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
