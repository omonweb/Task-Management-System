package com.app.service;

import com.app.dto.NotificationDTO;
import com.app.entity.Notification;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.NotificationRepository;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public List<NotificationDTO> getNotificationsByUserId(Integer userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        List<Notification> notifications = notificationRepository.findByUser_UserId(userId);

        List<NotificationDTO> result = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO dto = new NotificationDTO();

            dto.setNotificationId(notification.getNotificationId());
            dto.setText(notification.getText());
            dto.setCreatedAt(notification.getCreatedAt());

            if (notification.getUser() != null) {
                dto.setUserName(notification.getUser().getUsername());
                dto.setEmail(notification.getUser().getEmail());
            } else {
                dto.setUserName("Unknown User");
                dto.setEmail("Unknown Email");
            }

            result.add(dto);
        }

        return result;
    }
}