package com.enrollment.enrollmentservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// ============================================================
// REST TEMPLATE CONFIGURATION
// ============================================================
// RestTemplate = SYNCHRONOUS HTTP client (call and wait)
// Kafka        = ASYNCHRONOUS messaging (fire and forget)
//
// This service uses BOTH:
//   - RestTemplate to verify student/course exist (sync)
//   - Kafka to publish enrollment events (async)
//   - Kafka to consume payment events (async)
// ============================================================

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
