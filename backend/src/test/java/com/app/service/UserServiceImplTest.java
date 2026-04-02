package com.app.service;

import com.app.dto.UserDTO;
import com.app.dto.UserDetailsDTO;
import com.app.entity.*;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.ProjectRepository;
import com.app.repository.TaskRepository;
import com.app.repository.UserRepository;
import com.app.repository.UserRolesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRolesRepository userRolesRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @Test
    public void testGetUsersFiltered_WhenUserExists() {

        User user = new User();
        user.setUserId(1);
        user.setUsername("john_doe");
        user.setEmail("john@email.com");
        user.setFullName("John Doe");

        UserRole role = new UserRole();
        role.setRoleName("User");

        UserRoles userRoles = new UserRoles();
        userRoles.setUserRole(role);

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userRolesRepository.findByUserUserId(1))
                .thenReturn(List.of(userRoles));

        List<UserDTO> result = userService.getUsersFiltered("User", "john");

        assertEquals(1, result.size());
        assertEquals("john_doe", result.get(0).getUsername());
        assertEquals("User", result.get(0).getRoleName());
    }

    @Test
    public void testGetUsersFiltered_WhenNoUsersFound() {

        when(userRepository.findAll()).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUsersFiltered("User", "xyz")
        );

        assertEquals("No users found", exception.getMessage());
    }

    @Test
    public void testGetUserDetails_WhenUserExists() {

        User user = new User();
        user.setUserId(1);
        user.setUsername("john");

        when(userRepository.findById(1))
                .thenReturn(java.util.Optional.of(user));

        Project project = new Project();
        project.setProjectName("Project A");

        Task task = new Task();
        task.setTaskName("Task 1");

        when(projectRepository.findAll()).thenReturn(List.of(project));
        when(taskRepository.findAll()).thenReturn(List.of(task));

        UserDetailsDTO result = userService.getUserDetails(1);

        assertEquals("john", result.getUsername());
    }

    @Test
    public void testGetUserDetails_WhenUserNotFound() {

        when(userRepository.findById(999))
                .thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserDetails(999)
        );

        assertEquals("User not found with id: 999", exception.getMessage());
    }
}
