package com.app.service;

import com.app.dto.NotificationDTO;
import java.util.List;

public interface NotificationService {

    List<NotificationDTO> getNotificationsByUserId(Integer userId);
}