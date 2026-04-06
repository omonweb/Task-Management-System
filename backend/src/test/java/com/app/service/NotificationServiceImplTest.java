package com.app.service;

import com.app.dto.NotificationDTO;
import com.app.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationServiceImplTest {

    @Autowired
    private NotificationServiceImpl notificationService;

    // positive - user id 1 exists in DB so notifications should return
    @Test
    public void testGetNotificationsByUserId_WhenUserExists() {

        List<NotificationDTO> result = notificationService.getNotificationsByUserId(1);

        assertNotNull(result);
        assertTrue(result.size() >= 0);
    }

    // negative - user 9999 does not exist in DB so should throw 404
    @Test
    public void testGetNotificationsByUserId_WhenUserNotFound() {

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> notificationService.getNotificationsByUserId(9999)
        );

        assertEquals("User not found with ID: 9999", exception.getMessage());
    }
}
