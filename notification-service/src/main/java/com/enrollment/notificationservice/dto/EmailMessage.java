package com.enrollment.notificationservice.dto;

import java.io.Serializable;

// ============================================================
// EMAIL MESSAGE - The RabbitMQ message contract
// ============================================================
// This is sent FROM notification-service TO email-worker-service
// via RabbitMQ's email.queue.
//
// KEY DIFFERENCE from Kafka events:
//   - Kafka events say "something happened" (many consumers)
//   - RabbitMQ messages say "do this job" (one consumer picks it up)
//
// Implements Serializable for JSON serialization over RabbitMQ.
// ============================================================

public class EmailMessage implements Serializable {

    private String to;
    private String subject;
    private String body;
    private String notificationType;     // what triggered this email
    private Long referenceId;            // enrollment/payment ID for tracking

    public EmailMessage() {}

    public EmailMessage(String to, String subject, String body,
                        String notificationType, Long referenceId) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.notificationType = notificationType;
        this.referenceId = referenceId;
    }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }

    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }

    @Override
    public String toString() {
        return "EmailMessage{to='" + to + "', subject='" + subject +
                "', type='" + notificationType + "'}";
    }
}
