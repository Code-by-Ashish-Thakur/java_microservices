package com.enrollment.enrollmentservice.dto;

// ============================================================
// PAYMENT EVENT - Consumed FROM Kafka "payment-events"
// ============================================================
// This is the enrollment-service's COPY of PaymentEvent.
// The payment-service has its own copy (same structure).
//
// When a payment succeeds/fails, enrollment-service updates
// the enrollment status accordingly (Saga Pattern).
// ============================================================

public class PaymentEvent {

    private String eventType;       // PAYMENT_SUCCESS or PAYMENT_FAILED
    private Long paymentId;
    private Long enrollmentId;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long courseId;
    private String courseName;
    private double amount;
    private String transactionId;
    private long timestamp;

    public PaymentEvent() {}

    // Getters and Setters
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

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

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "PaymentEvent{eventType='" + eventType + "', enrollmentId=" + enrollmentId +
                ", amount=" + amount + ", txn='" + transactionId + "'}";
    }
}
