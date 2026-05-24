package com.enrollment.courseservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CourseServiceApplication.class, args);
        log.info("========================================");
        log.info("  COURSE SERVICE started on port 8083");
        log.info("  Kafka Producer: READY");
        log.info("  H2 Console: http://localhost:8083/h2-console");
        log.info("========================================");
    }
}
