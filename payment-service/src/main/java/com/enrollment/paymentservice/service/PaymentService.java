package com.enrollment.paymentservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enrollment.paymentservice.dto.EnrollmentEvent;
import com.enrollment.paymentservice.dto.PaymentEvent;
import com.enrollment.paymentservice.entity.Payment;
import com.enrollment.paymentservice.kafka.PaymentEventProducer;
import com.enrollment.paymentservice.repository.PaymentRepository;

// ============================================================
// PAYMENT SERVICE - Simulates payment processing
// ============================================================
// This service demonstrates:
//   1. Event-driven processing (reacts to Kafka events)
//   2. Simulated payment gateway (random success/failure)
//   3. Publishing result events back to Kafka
//
// FLOW:
//   1. Receive EnrollmentEvent from Kafka
//   2. Create Payment record (PENDING)
//   3. Simulate payment processing (2 second delay)
//   4. 90% chance SUCCESS, 10% chance FAILURE
//   5. Update Payment record
//   6. Publish PaymentEvent to Kafka
//
// In real world, step 3 would call Stripe/PayPal/Razorpay API.
// ============================================================

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer eventProducer;
    private final Random random = new Random();

    public PaymentService(PaymentRepository paymentRepository,
                          PaymentEventProducer eventProducer) {
        this.paymentRepository = paymentRepository;
        this.eventProducer = eventProducer;
    }

    public void processPayment(EnrollmentEvent enrollmentEvent) {
        log.info("[PAYMENT-SERVICE] ====== PAYMENT PROCESSING STARTED ======");
        log.info("[PAYMENT-SERVICE] Enrollment ID : {}", enrollmentEvent.getEnrollmentId());
        log.info("[PAYMENT-SERVICE] Student       : {} ({})", enrollmentEvent.getStudentName(), enrollmentEvent.getStudentEmail());
        log.info("[PAYMENT-SERVICE] Course        : {}", enrollmentEvent.getCourseName());
        log.info("[PAYMENT-SERVICE] Amount        : ${}", enrollmentEvent.getCoursePrice());

        // Step 1: Create payment record
        Payment payment = new Payment(
                enrollmentEvent.getEnrollmentId(),
                enrollmentEvent.getStudentId(),
                enrollmentEvent.getStudentName(),
                enrollmentEvent.getStudentEmail(),
                enrollmentEvent.getCourseId(),
                enrollmentEvent.getCourseName(),
                enrollmentEvent.getCoursePrice()
        );
        Payment saved = paymentRepository.save(payment);
        log.info("[PAYMENT-SERVICE] Payment record created with id: {}", saved.getId());

        // Step 2: Simulate payment processing delay
        log.info("[PAYMENT-SERVICE] Simulating payment gateway call (2 sec delay)...");
        try {
            Thread.sleep(2000); // 2 seconds to simulate gateway call
        } catch (InterruptedException e) {
            log.warn("[PAYMENT-SERVICE] Payment processing interrupted", e);
            Thread.currentThread().interrupt();
        }

        // Step 3: Simulate success/failure (90% success rate)
        boolean success = random.nextInt(10) < 9;

        if (success) {
            // Generate a fake transaction ID
            String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            saved.setStatus("SUCCESS");
            saved.setTransactionId(transactionId);
            saved.setProcessedAt(LocalDateTime.now());
            paymentRepository.save(saved);

            log.info("[PAYMENT-SERVICE] PAYMENT SUCCESS! TXN: {} | enrollmentId: {}", transactionId, saved.getEnrollmentId());

            // Publish success event
            PaymentEvent event = new PaymentEvent(
                    "PAYMENT_SUCCESS", saved.getId(), saved.getEnrollmentId(),
                    saved.getStudentId(), saved.getStudentName(), saved.getStudentEmail(),
                    saved.getCourseId(), saved.getCourseName(), saved.getAmount(),
                    transactionId
            );
            eventProducer.publish(event);

        } else {
            saved.setStatus("FAILED");
            saved.setFailureReason("Payment gateway declined the transaction");
            saved.setProcessedAt(LocalDateTime.now());
            paymentRepository.save(saved);

            log.warn("[PAYMENT-SERVICE] PAYMENT FAILED! enrollmentId: {} | reason: {}", saved.getEnrollmentId(), saved.getFailureReason());

            // Publish failure event
            PaymentEvent event = new PaymentEvent(
                    "PAYMENT_FAILED", saved.getId(), saved.getEnrollmentId(),
                    saved.getStudentId(), saved.getStudentName(), saved.getStudentEmail(),
                    saved.getCourseId(), saved.getCourseName(), saved.getAmount(),
                    null
            );
            eventProducer.publish(event);
        }

        log.info("[PAYMENT-SERVICE] ====== PAYMENT PROCESSING COMPLETE ======");
    }

    public List<Payment> getAllPayments() {
        log.info("[PAYMENT-SERVICE] Fetching all payments");
        List<Payment> payments = paymentRepository.findAll();
        log.info("[PAYMENT-SERVICE] Found {} payment(s)", payments.size());
        return payments;
    }

    public Payment getPaymentById(Long id) {
        log.info("[PAYMENT-SERVICE] Fetching payment by id: {}", id);
        return paymentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[PAYMENT-SERVICE] Payment not found with id: {}", id);
                    return new RuntimeException("Payment not found with id: " + id);
                });
    }

    public Payment getPaymentByEnrollmentId(Long enrollmentId) {
        log.info("[PAYMENT-SERVICE] Fetching payment by enrollmentId: {}", enrollmentId);
        return paymentRepository.findByEnrollmentId(enrollmentId)
                .orElseThrow(() -> {
                    log.error("[PAYMENT-SERVICE] Payment not found for enrollment: {}", enrollmentId);
                    return new RuntimeException("Payment not found for enrollment: " + enrollmentId);
                });
    }

    public List<Payment> getPaymentsByStudent(Long studentId) {
        log.info("[PAYMENT-SERVICE] Fetching payments for studentId: {}", studentId);
        List<Payment> payments = paymentRepository.findByStudentId(studentId);
        log.info("[PAYMENT-SERVICE] Found {} payment(s) for studentId: {}", payments.size(), studentId);
        return payments;
    }
}
