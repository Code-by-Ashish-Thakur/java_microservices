package com.enrollment.paymentservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.enrollment.paymentservice.dto.PaymentEvent;

@Component
public class PaymentEventProducer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventProducer.class);
    private static final String TOPIC = "payment-events";

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(PaymentEvent event) {
        log.info("[KAFKA-PRODUCER] >>> Publishing to topic '{}'", TOPIC);
        log.info("[KAFKA-PRODUCER]     eventType    : {}", event.getEventType());
        log.info("[KAFKA-PRODUCER]     enrollmentId : {}", event.getEnrollmentId());
        log.info("[KAFKA-PRODUCER]     amount       : ${}", event.getAmount());
        kafkaTemplate.send(TOPIC, String.valueOf(event.getEnrollmentId()), event);
        log.info("[KAFKA-PRODUCER] >>> Event published successfully to '{}'", TOPIC);
    }
}
