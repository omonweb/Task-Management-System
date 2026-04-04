package com.app.service;

import com.app.dto.UserDTO;
import com.app.dto.UserDetailsDTO;
import com.app.entity.*;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceImplTest {

    // 1. Use @Autowired to bring in the REAL service and repositories
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testGetUsersFiltered_WhenUserExists() {
        // ARRANGE
        User user = new User();
        user.setUserId(101); // <--- MANUALLY ASSIGN ID HERE
        user.setUsername("integration_john");
        user.setEmail("john@email.com");
        user.setFullName("John Doe");
        user.setPassword("dummyPassword");
        User savedUser = userRepository.save(user);

        UserRole role = new UserRole();
        role.setUserRoleId(101); // <--- MANUALLY ASSIGN ID HERE
        role.setRoleName("User");
        UserRole savedRole = userRoleRepository.save(role);

        UserRolesId bridgeId = new UserRolesId();
        bridgeId.setUserId(savedUser.getUserId());
        bridgeId.setUserRoleId(savedRole.getUserRoleId());

        UserRoles userRoles = new UserRoles();
        userRoles.setId(bridgeId);
        userRoles.setUser(savedUser);
        userRoles.setUserRole(savedRole);
        userRolesRepository.save(userRoles);

        // ACT
        List<UserDTO> result = userService.getUsersFiltered("User", "integration_john");

        // ASSERT
        assertFalse(result.isEmpty(), "Result should not be empty");
        assertEquals("integration_john", result.get(0).getUsername());
        assertEquals("User", result.get(0).getRoleName());
    }

    @Test
    public void testGetUsersFiltered_WhenNoUsersFound() {
        // ACT & ASSERT: Search for a completely random name that isn't in the DB
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUsersFiltered("User", "xyz_random_not_found")
        );

        assertEquals("No users found", exception.getMessage());
    }

    @Test
    public void testGetUserDetails_WhenUserExists() {
        // ARRANGE
        User user = new User();
        user.setUserId(102);
        user.setUsername("integration_jane");
        user.setPassword("dummyPassword");

        // --- ADD THESE TWO LINES ---
        user.setEmail("jane@email.com");
        user.setFullName("Jane Doe");
        // ---------------------------

        User savedUser = userRepository.save(user);

        Project project = new Project();
        project.setProjectId(102);
        project.setProjectName("Project A");
        projectRepository.save(project);

        Task task = new Task();
        task.setTaskId(102);
        task.setTaskName("Task 1");
        taskRepository.save(task);

        // ACT
        UserDetailsDTO result = userService.getUserDetails(savedUser.getUserId());

        // ASSERT
        assertNotNull(result);
        assertEquals("integration_jane", result.getUsername());
    }

    @Test
    public void testGetUserDetails_WhenUserNotFound() {
        // ARRANGE: Use an ID that will likely never exist
        Integer fakeId = 9999999;

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserDetails(fakeId)
        );

        assertEquals("User not found with id: " + fakeId, exception.getMessage());
    }
}
