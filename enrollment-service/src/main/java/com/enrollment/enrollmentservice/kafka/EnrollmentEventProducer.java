package com.enrollment.enrollmentservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.enrollment.enrollmentservice.dto.EnrollmentEvent;

@Component
public class EnrollmentEventProducer {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentEventProducer.class);

    private static final String TOPIC = "enrollment-events";

    private final KafkaTemplate<String, EnrollmentEvent> kafkaTemplate;

    public EnrollmentEventProducer(KafkaTemplate<String, EnrollmentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEnrollmentCreated(EnrollmentEvent event) {
        log.info("[KAFKA-PRODUCER] >>> Publishing to topic '{}' | eventType={} | enrollmentId={}",
                TOPIC, event.getEventType(), event.getEnrollmentId());
        kafkaTemplate.send(TOPIC, String.valueOf(event.getEnrollmentId()), event);
        log.info("[KAFKA-PRODUCER] Event published successfully to '{}'", TOPIC);
    }
}
