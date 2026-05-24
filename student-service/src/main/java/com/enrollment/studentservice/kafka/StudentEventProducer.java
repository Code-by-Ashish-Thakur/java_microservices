package com.enrollment.studentservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.enrollment.studentservice.dto.StudentEvent;

@Component
public class StudentEventProducer {

    private static final Logger log = LoggerFactory.getLogger(StudentEventProducer.class);
    private static final String TOPIC = "student-events";

    private final KafkaTemplate<String, StudentEvent> kafkaTemplate;

    public StudentEventProducer(KafkaTemplate<String, StudentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishStudentCreated(StudentEvent event) {
        log.info("[KAFKA-PRODUCER] >>> Publishing to topic '{}' | eventType={} | studentId={} | email={}",
                TOPIC, event.getEventType(), event.getStudentId(), event.getEmail());
        kafkaTemplate.send(TOPIC, String.valueOf(event.getStudentId()), event);
        log.info("[KAFKA-PRODUCER] >>> Successfully published STUDENT_CREATED event to Kafka");
    }
}
