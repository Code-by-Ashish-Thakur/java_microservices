package com.enrollment.notificationservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// ============================================================
// NOTIFICATION ENTITY - Logs every notification sent
// ============================================================
// Every event that triggers a notification is persisted here.
// This gives us an audit trail and allows querying notification
// history via REST API.
// ============================================================

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;            // STUDENT_CREATED, ENROLLMENT_CREATED, PAYMENT_SUCCESS, etc.
    private String channel;         // EMAIL, SMS, PUSH
    private String recipient;       // email address
    private String subject;
    private String message;
    private String status;          // QUEUED, SENT, FAILED

    private LocalDateTime createdAt;

    public Notification() {}

    public Notification(String type, String channel, String recipient,
                        String subject, String message) {
        this.type = type;
        this.channel = channel;
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.status = "QUEUED";
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
