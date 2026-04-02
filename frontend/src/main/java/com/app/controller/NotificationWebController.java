package com.app.controller;

import com.app.dto.NotificationDTO;
import com.app.service.NotificationClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationWebController {

    private final NotificationClientService notificationClientService;

    // Default userId=1 so the page works without manual input on first load.
    // In a real app with login, this would come from the security context.
    @GetMapping("/notifications")
    public String viewNotifications(
            @RequestParam(defaultValue = "1") Integer userId,
            Model model) {

        List<NotificationDTO> notifications =
                notificationClientService.fetchNotificationsByUserId(userId);

        model.addAttribute("notifications", notifications);
        model.addAttribute("userId", userId);
        model.addAttribute("count", notifications.size());

        return "notifications";
    }
}
