package com.enrollment.studentservice.dto;

// ============================================================
// STUDENT EVENT - The Kafka message contract
// ============================================================
// Published to "student-events" topic when a student is created.
// Consumed by: notification-service
//
// This class must have the SAME structure in both the producer
// and consumer services (they each have their own copy).
// ============================================================

public class StudentEvent {

    private String eventType;    // e.g., "STUDENT_CREATED"
    private Long studentId;
    private String name;
    private String email;
    private String phone;
    private long timestamp;

    public StudentEvent() {}

    public StudentEvent(String eventType, Long studentId, String name, String email, String phone) {
        this.eventType = eventType;
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.timestamp = System.currentTimeMillis();
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "StudentEvent{eventType='" + eventType + "', studentId=" + studentId +
                ", name='" + name + "', email='" + email + "'}";
    }
}
