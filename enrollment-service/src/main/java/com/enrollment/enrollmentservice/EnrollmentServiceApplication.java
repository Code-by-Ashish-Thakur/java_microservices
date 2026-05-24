package com.enrollment.enrollmentservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EnrollmentServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EnrollmentServiceApplication.class, args);
        log.info("========================================");
        log.info("  Enrollment Service started on port 8084");
        log.info("  Kafka Producer + Consumer ready!");
        log.info("========================================");
    }
}
