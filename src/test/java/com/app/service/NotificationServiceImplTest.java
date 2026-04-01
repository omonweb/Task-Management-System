package com.app.service;

import com.app.dto.NotificationDTO;
import com.app.entity.Notification;
import com.app.entity.User;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.NotificationRepository;
import com.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void getNotificationsByUserId_whenUserExists_shouldReturnNotifications() {

        //fake User
        User fakeUser = new User();
        fakeUser.setUserId(1);
        fakeUser.setUsername("alice");
        fakeUser.setEmail("alice@example.com");

        //fake Notification linked to that user
        Notification fakeNotification = new Notification();
        fakeNotification.setNotificationId(1);
        fakeNotification.setText("You have been assigned a new task");
        fakeNotification.setCreatedAt(LocalDateTime.of(2022, 1, 5, 12, 0, 0));
        fakeNotification.setUser(fakeUser);

        when(userRepository.existsById(1)).thenReturn(true);
        when(notificationRepository.findByUser_UserId(1)).thenReturn(List.of(fakeNotification));

        List<NotificationDTO> result = notificationService.getNotificationsByUserId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getNotificationId());
        assertEquals("You have been assigned a new task", result.get(0).getText());
        assertEquals("alice", result.get(0).getUserName());
        assertEquals("alice@example.com", result.get(0).getEmail());
    }

    @Test
    void getNotificationsByUserId_whenUserNotFound_shouldThrowException() {

        when(userRepository.existsById(9999)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.getNotificationsByUserId(9999);
        });
    }
}
