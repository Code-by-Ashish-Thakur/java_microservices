package com.enrollment.enrollmentservice.dto;

// ============================================================
// ENROLLMENT EVENT - Published to Kafka "enrollment-events"
// ============================================================
// Consumed by: payment-service, notification-service
//
// Contains enough data for payment-service to create a payment
// without needing to call other services.
// ============================================================

public class EnrollmentEvent {

    private String eventType;       // ENROLLMENT_CREATED
    private Long enrollmentId;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long courseId;
    private String courseName;
    private double coursePrice;
    private long timestamp;

    public EnrollmentEvent() {}

    public EnrollmentEvent(String eventType, Long enrollmentId,
                           Long studentId, String studentName, String studentEmail,
                           Long courseId, String courseName, double coursePrice) {
        this.eventType = eventType;
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.courseId = courseId;
        this.courseName = courseName;
        this.coursePrice = coursePrice;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public double getCoursePrice() { return coursePrice; }
    public void setCoursePrice(double coursePrice) { this.coursePrice = coursePrice; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "EnrollmentEvent{eventType='" + eventType + "', enrollmentId=" + enrollmentId +
                ", student='" + studentName + "', course='" + courseName +
                "', price=" + coursePrice + "}";
    }
}
