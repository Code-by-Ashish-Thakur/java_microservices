package com.enrollment.paymentservice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.enrollment.paymentservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByEnrollmentId(Long enrollmentId);

    List<Payment> findByStudentId(Long studentId);

    List<Payment> findByStatus(String status);
}
