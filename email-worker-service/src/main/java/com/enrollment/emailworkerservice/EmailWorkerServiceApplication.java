package com.enrollment.emailworkerservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmailWorkerServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(EmailWorkerServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EmailWorkerServiceApplication.class, args);
        log.info("========================================");
        log.info("  Email Worker Service on port 8086");
        log.info("  RabbitMQ Consumer ready!");
        log.info("  Listening on email.queue...");
        log.info("========================================");
    }
}
