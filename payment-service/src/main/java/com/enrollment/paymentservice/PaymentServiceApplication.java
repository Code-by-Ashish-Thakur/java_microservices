package com.enrollment.paymentservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
        log.info("========================================");
        log.info("  Payment Service started on port 8085");
        log.info("  Kafka Consumer + Producer ready!");
        log.info("  Listening for enrollment-events...");
        log.info("========================================");
    }
}
