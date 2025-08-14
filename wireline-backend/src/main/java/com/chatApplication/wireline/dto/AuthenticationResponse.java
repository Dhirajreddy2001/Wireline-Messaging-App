package com.chatApplication.wireline.dto;

public class AuthenticationResponse {

    private String message;
    private String userId;
    private String sessionId;
    
    public AuthenticationResponse(String message)
    {
        this.message = message;
    }

    public AuthenticationResponse(String message, String userId,String sessionId) {
        this.message = message;
        this.userId = userId;
        this.sessionId = sessionId;
    }

     public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getSessionId()
    {
        return this.sessionId;
    }
    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    
}
