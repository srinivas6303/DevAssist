package com.srinivas.devassist.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime timestamp;

    public ChatMessage() {}

    public ChatMessage(User user, Role role, String message, LocalDateTime timestamp) {
        this.user = user;
        this.role = role;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Role getRole() { return role; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setUser(User user) { this.user = user; }
    public void setRole(Role role) { this.role = role; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}