package com.app.controller;

import com.app.dto.NotificationDTO;
import com.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    //GET /api/notifications?userId=1
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(
            @RequestParam Integer userId) {

        List<NotificationDTO> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }
}
