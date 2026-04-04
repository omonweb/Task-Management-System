package com.app.service;

import com.app.dto.UserDTO;
import com.app.dto.UserDetailsDTO;
import com.app.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    // ---------------------------------------------------------
    // API 1: getUsersFiltered
    // ---------------------------------------------------------

    @Test
    public void testGetUsersFiltered_UsingExistingData() {
        // ACT
        List<UserDTO> result = userService.getUsersFiltered("User", "john_doe");

        // ASSERT
        assertFalse(result.isEmpty(), "Could not find the user in the database! Did you type the right name?");
        assertEquals("john_doe", result.get(0).getUsername());
        assertEquals("User", result.get(0).getRoleName());
    }

    @Test
    public void testGetUsersFiltered_WhenNoUsersFound() {
        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUsersFiltered("User", "this_user_does_not_exist_123")
        );

        assertEquals("No users found", exception.getMessage());
    }

    // ---------------------------------------------------------
    // API 2: getUserDetails
    // ---------------------------------------------------------

    @Test
    public void testGetUserDetails_UsingExistingData() {
        Integer existingUserId = 1;

        // ACT
        UserDetailsDTO result = userService.getUserDetails(existingUserId);

        // ASSERT
        assertNotNull(result, "The service returned null for this ID!");
        assertTrue(result.getUsername() != null && !result.getUsername().isEmpty());
    }

    @Test
    public void testGetUserDetails_WhenUserNotFound() {
        // ARRANGE
        Integer fakeId = 9999999;

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserDetails(fakeId)
        );

        assertEquals("User not found with id: " + fakeId, exception.getMessage());
    }
}
