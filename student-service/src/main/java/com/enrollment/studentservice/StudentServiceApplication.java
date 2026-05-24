package com.enrollment.studentservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(StudentServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(StudentServiceApplication.class, args);
        log.info("========================================");
        log.info("  STUDENT SERVICE started on port 8081");
        log.info("  Kafka Producer: READY");
        log.info("  H2 Console: http://localhost:8081/h2-console");
        log.info("========================================");
    }
}
