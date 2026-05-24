package com.enrollment.notificationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
        log.info("========================================");
        log.info("  Notification Service on port 8082");
        log.info("  Kafka Consumer + RabbitMQ Producer!");
        log.info("========================================");
    }
}
