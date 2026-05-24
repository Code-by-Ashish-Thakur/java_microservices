package com.enrollment.notificationservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.enrollment.notificationservice.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipient(String recipient);

    List<Notification> findByType(String type);

    List<Notification> findByStatus(String status);
}
