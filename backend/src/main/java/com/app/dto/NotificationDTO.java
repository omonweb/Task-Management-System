package com.app.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {

    private Integer notificationId;
    private String text;
    private LocalDateTime createdAt;
    private String userName;
    private String email;
}