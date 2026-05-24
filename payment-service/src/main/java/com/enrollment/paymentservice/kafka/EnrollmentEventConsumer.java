package com.enrollment.paymentservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.enrollment.paymentservice.dto.EnrollmentEvent;
import com.enrollment.paymentservice.service.PaymentService;

// ============================================================
// ENROLLMENT EVENT CONSUMER - Triggers payment processing
// ============================================================
// Listens to "enrollment-events" topic.
// When a student enrolls, this consumer automatically creates
// and processes a payment — no REST call needed!
//
// This is PURE EVENT-DRIVEN ARCHITECTURE:
//   - enrollment-service doesn't know payment-service exists
//   - payment-service reacts to events on its own
//   - Loose coupling between services
//
// If payment-service is down when enrollment happens:
//   - Kafka retains the message
//   - When payment-service starts up, it processes the backlog
//   - This is "guaranteed delivery" via Kafka
// ============================================================

@Component
public class EnrollmentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentEventConsumer.class);

    private final PaymentService paymentService;

    public EnrollmentEventConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "enrollment-events", groupId = "payment-group")
    public void handleEnrollmentEvent(EnrollmentEvent event) {
        log.info("[KAFKA-CONSUMER] <<< Received enrollment event from topic 'enrollment-events'");
        log.info("[KAFKA-CONSUMER]     eventType    : {}", event.getEventType());
        log.info("[KAFKA-CONSUMER]     enrollmentId : {}", event.getEnrollmentId());
        log.info("[KAFKA-CONSUMER]     student      : {}", event.getStudentName());
        log.info("[KAFKA-CONSUMER]     course       : {}", event.getCourseName());
        log.info("[KAFKA-CONSUMER]     amount       : ${}", event.getCoursePrice());

        if ("ENROLLMENT_CREATED".equals(event.getEventType())) {
            log.info("[KAFKA-CONSUMER] Event type is ENROLLMENT_CREATED - delegating to PaymentService");
            paymentService.processPayment(event);
        } else {
            log.info("[KAFKA-CONSUMER] Ignoring event with type: {}", event.getEventType());
        }
    }
}
