package com.enrollment.enrollmentservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.enrollment.enrollmentservice.dto.PaymentEvent;
import com.enrollment.enrollmentservice.repository.EnrollmentRepository;

// ============================================================
// PAYMENT EVENT CONSUMER - Saga Pattern completion
// ============================================================
// This consumer listens to "payment-events" from payment-service.
//
// SAGA FLOW:
//   1. enrollment-service creates enrollment (PENDING)
//   2. payment-service processes payment asynchronously
//   3. THIS CONSUMER receives payment result
//   4. Updates enrollment status to CONFIRMED or FAILED
//
// This is the "choreography-based saga" pattern:
//   - No central orchestrator
//   - Each service reacts to events from other services
//   - Loose coupling between services
// ============================================================

@Component
public class PaymentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class);

    private final EnrollmentRepository enrollmentRepository;

    public PaymentEventConsumer(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @KafkaListener(topics = "payment-events", groupId = "enrollment-group")
    public void handlePaymentEvent(PaymentEvent event) {

        log.info("[KAFKA-CONSUMER] <<< Received payment event from topic 'payment-events'");
        log.info("[KAFKA-CONSUMER]     eventType    : {}", event.getEventType());
        log.info("[KAFKA-CONSUMER]     enrollmentId : {}", event.getEnrollmentId());
        log.info("[KAFKA-CONSUMER]     amount       : ${}", event.getAmount());
        log.info("[KAFKA-CONSUMER]     transactionId: {}", event.getTransactionId());

        // Find the enrollment and update its status
        enrollmentRepository.findById(event.getEnrollmentId()).ifPresent(enrollment -> {
            if ("PAYMENT_SUCCESS".equals(event.getEventType())) {
                enrollment.setStatus("CONFIRMED");
                log.info("[SAGA-COMPLETE] Enrollment #{} status updated: PENDING -> CONFIRMED", enrollment.getId());
            } else if ("PAYMENT_FAILED".equals(event.getEventType())) {
                enrollment.setStatus("FAILED");
                log.warn("[SAGA-FAILED] Enrollment #{} status updated: PENDING -> FAILED", enrollment.getId());
            }
            enrollmentRepository.save(enrollment);
        });
    }
}
