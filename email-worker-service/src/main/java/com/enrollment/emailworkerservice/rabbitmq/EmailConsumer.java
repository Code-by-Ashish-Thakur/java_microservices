package com.enrollment.emailworkerservice.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.enrollment.emailworkerservice.dto.EmailMessage;

// ============================================================
// EMAIL CONSUMER - The actual email "sender" (simulated)
// ============================================================
// This is a BACKGROUND WORKER that:
//   1. Picks up EmailMessage from RabbitMQ email.queue
//   2. Simulates sending the email (1 second delay)
//   3. Logs the result
//
// KEY RABBITMQ CONCEPTS DEMONSTRATED:
//
// 1. MESSAGE ACKNOWLEDGMENT:
//    By default, Spring AMQP auto-acks after successful processing.
//    If this method throws an exception, the message is NACK'd
//    and retried (based on retry config in application.yml).
//
// 2. DEAD LETTER QUEUE:
//    After max retries (3), the message goes to email.dlq.
//    Ops team can inspect failed messages and retry manually.
//
// 3. COMPETING CONSUMERS:
//    Run multiple instances of this service — RabbitMQ
//    automatically distributes messages round-robin.
//
// 4. PREFETCH:
//    prefetch=1 means this consumer only takes one message
//    at a time, ensuring fair distribution across instances.
//
// In production, replace the simulation with:
//   - JavaMailSender (SMTP)
//   - SendGrid API
//   - AWS SES
//   - Twilio SendGrid
// ============================================================

@Component
public class EmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    @RabbitListener(queues = "email.queue")
    public void processEmail(EmailMessage message) {
        log.info("[RABBITMQ-CONSUMER] <<< Picked up email task from queue 'email.queue'");
        log.info("[RABBITMQ-CONSUMER]     To      : {}", message.getTo());
        log.info("[RABBITMQ-CONSUMER]     Subject : {}", message.getSubject());
        log.info("[RABBITMQ-CONSUMER]     Type    : {}", message.getNotificationType());

        // Simulate email sending (1 second delay)
        log.info("[EMAIL-WORKER] Simulating email send (1 sec delay)...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate the email body
        log.info("[EMAIL-WORKER] ====== EMAIL SENT ======");
        log.info("[EMAIL-WORKER] To      : {}", message.getTo());
        log.info("[EMAIL-WORKER] Subject : {}", message.getSubject());
        log.info("[EMAIL-WORKER] Body    : {}", message.getBody());
        log.info("[EMAIL-WORKER] ========================");
        log.info("[EMAIL-WORKER] Email delivered successfully!");
    }
}
