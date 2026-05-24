package com.enrollment.emailworkerservice.dto;

import java.io.Serializable;

// Copy of EmailMessage - must match the producer's structure

public class EmailMessage implements Serializable {

    private String to;
    private String subject;
    private String body;
    private String notificationType;
    private Long referenceId;

    public EmailMessage() {}

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
