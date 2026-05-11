package com.srinivas.devassist.dto;

public class ChatResponse {
    private String message;
    private String role;

    public ChatResponse() {
    }

    public ChatResponse(String role,String message) {
        this.message = message;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ChatResponse{" +
                ", role='" + role + '\'' +
                "message='" + message + '\'' +
                '}';
    }
}
