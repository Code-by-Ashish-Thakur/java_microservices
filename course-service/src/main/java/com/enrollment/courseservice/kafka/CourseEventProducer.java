package com.enrollment.courseservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.enrollment.courseservice.dto.CourseEvent;

@Component
public class CourseEventProducer {

    private static final Logger log = LoggerFactory.getLogger(CourseEventProducer.class);
    private static final String TOPIC = "course-events";

    private final KafkaTemplate<String, CourseEvent> kafkaTemplate;

    public CourseEventProducer(KafkaTemplate<String, CourseEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(CourseEvent event) {
        log.info("[KAFKA-PRODUCER] >>> Publishing to topic '{}' | eventType={} | courseId={} | name={}",
                TOPIC, event.getEventType(), event.getCourseId(), event.getName());
        kafkaTemplate.send(TOPIC, String.valueOf(event.getCourseId()), event);
        log.info("[KAFKA-PRODUCER] >>> Successfully published {} event to Kafka", event.getEventType());
    }
}
