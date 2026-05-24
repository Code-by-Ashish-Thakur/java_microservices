package com.enrollment.notificationservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.enrollment.notificationservice.entity.Notification;
import com.enrollment.notificationservice.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // GET /api/notifications - all notification logs
    @GetMapping
    public List<Notification> getAll() {
        log.info("[NOTIFICATION-CONTROLLER] GET /api/notifications - fetching all notifications");
        return notificationService.getAllNotifications();
    }

    // GET /api/notifications/recipient/{email} - by recipient
    @GetMapping("/recipient/{email}")
    public List<Notification> getByRecipient(@PathVariable String email) {
        log.info("[NOTIFICATION-CONTROLLER] GET /api/notifications/recipient/{} - fetching by recipient", email);
        return notificationService.getByRecipient(email);
    }

    // GET /api/notifications/type/{type} - by event type
    @GetMapping("/type/{type}")
    public List<Notification> getByType(@PathVariable String type) {
        log.info("[NOTIFICATION-CONTROLLER] GET /api/notifications/type/{} - fetching by type", type);
        return notificationService.getByType(type);
    }
}
