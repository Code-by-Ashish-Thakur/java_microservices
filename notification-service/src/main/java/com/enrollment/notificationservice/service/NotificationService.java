package com.enrollment.notificationservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enrollment.notificationservice.dto.EmailMessage;
import com.enrollment.notificationservice.entity.Notification;
import com.enrollment.notificationservice.rabbitmq.EmailMessageProducer;
import com.enrollment.notificationservice.repository.NotificationRepository;

// ============================================================
// NOTIFICATION SERVICE - Business logic
// ============================================================
// Two responsibilities:
//   1. Persist notification records to H2 database (audit trail)
//   2. Dispatch email tasks to RabbitMQ (actual email sending)
// ============================================================

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final EmailMessageProducer emailMessageProducer;

    public NotificationService(NotificationRepository notificationRepository,
                               EmailMessageProducer emailMessageProducer) {
        this.notificationRepository = notificationRepository;
        this.emailMessageProducer = emailMessageProducer;
    }

    public Notification createAndDispatch(String type, String recipient,
                                           String subject, String body, Long referenceId) {
        log.info("[NOTIFICATION-SERVICE] Creating notification | type={} | recipient={}", type, recipient);

        // Step 1: Save notification record
        Notification notification = new Notification(type, "EMAIL", recipient, subject, body);
        Notification saved = notificationRepository.save(notification);
        log.info("[NOTIFICATION-SERVICE] Notification saved to DB with id: {}", saved.getId());

        // Step 2: Dispatch email task to RabbitMQ
        log.info("[NOTIFICATION-SERVICE] Dispatching email task to RabbitMQ...");
        EmailMessage emailMessage = new EmailMessage(recipient, subject, body, type, referenceId);
        emailMessageProducer.sendEmailTask(emailMessage);

        return saved;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getByRecipient(String recipient) {
        return notificationRepository.findByRecipient(recipient);
    }

    public List<Notification> getByType(String type) {
        return notificationRepository.findByType(type);
    }
}
