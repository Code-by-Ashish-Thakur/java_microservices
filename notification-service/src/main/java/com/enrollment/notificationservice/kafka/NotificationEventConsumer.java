package com.enrollment.notificationservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.enrollment.notificationservice.dto.*;
import com.enrollment.notificationservice.service.NotificationService;

// ============================================================
// KAFKA EVENT CONSUMER - Listens to ALL event topics
// ============================================================
// This is the notification-service's "ear" on the Kafka bus.
// It listens to 4 topics and creates notifications for each.
//
// For each event:
//   1. Log the event (console)
//   2. Create a Notification record (H2 database)
//   3. Dispatch an email task (RabbitMQ → email-worker-service)
//
// TOPICS CONSUMED:
//   - student-events    → Welcome email
//   - course-events     → Course notification
//   - enrollment-events → Enrollment confirmation
//   - payment-events    → Payment receipt / failure notice
// ============================================================

@Component
public class NotificationEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);

    private final NotificationService notificationService;

    public NotificationEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ==================== STUDENT EVENTS ====================
    @KafkaListener(topics = "student-events", groupId = "notification-group")
    public void handleStudentEvent(StudentEvent event) {
        log.info("[KAFKA-CONSUMER] <<< Received STUDENT event | type={} | name={}", event.getEventType(), event.getName());

        if ("STUDENT_CREATED".equals(event.getEventType())) {
            notificationService.createAndDispatch(
                    "STUDENT_CREATED",
                    event.getEmail(),
                    "Welcome to the Student Enrollment System!",
                    "Hi " + event.getName() + ", your account has been created successfully. " +
                            "You can now browse and enroll in courses.",
                    event.getStudentId()
            );
        }
    }

    // ==================== COURSE EVENTS ====================
    @KafkaListener(topics = "course-events", groupId = "notification-group")
    public void handleCourseEvent(CourseEvent event) {
        log.info("[KAFKA-CONSUMER] <<< Received COURSE event | type={} | name={}", event.getEventType(), event.getName());

        // Course events are logged but don't trigger emails (no recipient)
        // In production, you might notify subscribed students
    }

    // ==================== ENROLLMENT EVENTS ====================
    @KafkaListener(topics = "enrollment-events", groupId = "notification-group")
    public void handleEnrollmentEvent(EnrollmentEvent event) {
        log.info("[KAFKA-CONSUMER] <<< Received ENROLLMENT event | type={} | student={} | course={}", event.getEventType(), event.getStudentName(), event.getCourseName());

        if ("ENROLLMENT_CREATED".equals(event.getEventType())) {
            notificationService.createAndDispatch(
                    "ENROLLMENT_CREATED",
                    event.getStudentEmail(),
                    "Enrollment Pending - " + event.getCourseName(),
                    "Hi " + event.getStudentName() + ", your enrollment in '" +
                            event.getCourseName() + "' is pending payment. " +
                            "Amount: $" + event.getCoursePrice(),
                    event.getEnrollmentId()
            );
        }
    }

    // ==================== PAYMENT EVENTS ====================
    @KafkaListener(topics = "payment-events", groupId = "notification-group")
    public void handlePaymentEvent(PaymentEvent event) {
        log.info("[KAFKA-CONSUMER] <<< Received PAYMENT event | type={} | student={} | amount=${}", event.getEventType(), event.getStudentName(), event.getAmount());

        if ("PAYMENT_SUCCESS".equals(event.getEventType())) {
            notificationService.createAndDispatch(
                    "PAYMENT_SUCCESS",
                    event.getStudentEmail(),
                    "Payment Successful - " + event.getCourseName(),
                    "Hi " + event.getStudentName() + ", your payment of $" +
                            event.getAmount() + " for '" + event.getCourseName() +
                            "' was successful! Transaction ID: " + event.getTransactionId() +
                            ". You are now enrolled.",
                    event.getPaymentId()
            );
        } else if ("PAYMENT_FAILED".equals(event.getEventType())) {
            notificationService.createAndDispatch(
                    "PAYMENT_FAILED",
                    event.getStudentEmail(),
                    "Payment Failed - " + event.getCourseName(),
                    "Hi " + event.getStudentName() + ", your payment of $" +
                            event.getAmount() + " for '" + event.getCourseName() +
                            "' has failed. Please try again.",
                    event.getPaymentId()
            );
        }
    }
}
