package com.enrollment.paymentservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.enrollment.paymentservice.entity.Payment;
import com.enrollment.paymentservice.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // GET /api/payments - all payments
    @GetMapping
    public List<Payment> getAllPayments() {
        log.info("[PAYMENT-CONTROLLER] GET /api/payments - Fetching all payments");
        List<Payment> payments = paymentService.getAllPayments();
        log.info("[PAYMENT-CONTROLLER] Returning {} payment(s)", payments.size());
        return payments;
    }

    // GET /api/payments/{id} - single payment
    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        log.info("[PAYMENT-CONTROLLER] GET /api/payments/{} - Fetching payment by id", id);
        Payment payment = paymentService.getPaymentById(id);
        log.info("[PAYMENT-CONTROLLER] Found payment with id: {}, status: {}", id, payment.getStatus());
        return payment;
    }

    // GET /api/payments/enrollment/{enrollmentId} - payment for an enrollment
    @GetMapping("/enrollment/{enrollmentId}")
    public Payment getByEnrollment(@PathVariable Long enrollmentId) {
        log.info("[PAYMENT-CONTROLLER] GET /api/payments/enrollment/{} - Fetching payment by enrollmentId", enrollmentId);
        Payment payment = paymentService.getPaymentByEnrollmentId(enrollmentId);
        log.info("[PAYMENT-CONTROLLER] Found payment for enrollmentId: {}, status: {}", enrollmentId, payment.getStatus());
        return payment;
    }

    // GET /api/payments/student/{studentId} - all payments for a student
    @GetMapping("/student/{studentId}")
    public List<Payment> getByStudent(@PathVariable Long studentId) {
        log.info("[PAYMENT-CONTROLLER] GET /api/payments/student/{} - Fetching payments by studentId", studentId);
        List<Payment> payments = paymentService.getPaymentsByStudent(studentId);
        log.info("[PAYMENT-CONTROLLER] Returning {} payment(s) for studentId: {}", payments.size(), studentId);
        return payments;
    }
}
