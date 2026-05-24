package com.enrollment.notificationservice.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.enrollment.notificationservice.config.RabbitMQConfig;
import com.enrollment.notificationservice.dto.EmailMessage;

// ============================================================
// RABBITMQ PRODUCER - Sends email tasks to email.queue
// ============================================================
// This producer sends EmailMessage objects to RabbitMQ.
//
// KEY DIFFERENCE FROM KAFKA PRODUCER:
//   Kafka: publish to a topic, MANY consumers can read
//   RabbitMQ: send to a queue, EXACTLY ONE consumer picks it up
//
// The email-worker-service will consume these messages and
// simulate sending the actual email.
//
// FLOW:
//   Kafka event arrives → notification-service processes it
//   → saves Notification to DB → dispatches EmailMessage to RabbitMQ
//   → email-worker-service picks it up and "sends" the email
// ============================================================

@Component
public class EmailMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(EmailMessageProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public EmailMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmailTask(EmailMessage emailMessage) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                emailMessage
        );
        log.info("[RABBITMQ-PRODUCER] >>> Dispatching email task to queue '{}' | to={} | subject={}", RabbitMQConfig.EMAIL_QUEUE, emailMessage.getTo(), emailMessage.getSubject());
    }
}
