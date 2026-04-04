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

    @GetMapping("/notifications")
    public String viewNotifications(
            @RequestParam(defaultValue = "1") Integer userId,
            Model model) {

        try {
            List<NotificationDTO> notifications =
                    notificationClientService.fetchNotificationsByUserId(userId);

            model.addAttribute("notifications", notifications);
            model.addAttribute("count", notifications.size());
            model.addAttribute("error", null);

        } catch (IllegalArgumentException e) {
            model.addAttribute("notifications", List.of());
            model.addAttribute("count", 0);
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("notifications", List.of());
            model.addAttribute("count", 0);
            model.addAttribute("error", "Something went wrong. Please try again.");
        }

        model.addAttribute("userId", userId);
        return "notifications";
    }
}
