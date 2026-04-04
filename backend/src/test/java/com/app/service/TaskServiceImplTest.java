package com.app.service;

import com.app.dto.TaskDTO;
import com.app.dto.TaskDetailsDTO;
import com.app.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
@Transactional
public class TaskServiceImplTest {

    @Autowired
    private TaskServiceImpl taskService;

    // =========================================================
    // API 1: getTasks (Paginated & Filtered)
    // =========================================================

    @Test
    void testGetTasks_success() { // POSITIVE
        Page<TaskDTO> result = taskService.getTasks(null, null, 0, 5);

        Assertions.assertNotNull(result);

        // If the DB has data, verify the fields mapped correctly
        if (result.getTotalElements() > 0) {
            TaskDTO dto = result.getContent().get(0);
            Assertions.assertNotNull(dto.getTaskId());
            Assertions.assertNotNull(dto.getTaskName());
            Assertions.assertNotNull(dto.getStatus());
        }
    }

    @Test
    void testGetTasks_invalidPagination() { // NEGATIVE
        // Spring Data JPA throws an IllegalArgumentException if page size is < 1
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                taskService.getTasks(null, null, 0, 0)
        );
    }

    // =========================================================
    // API 2: getTaskById (Deep Dive Details)
    // =========================================================

    @Test
    void testGetTaskById_success() { // POSITIVE
        // Dynamically grab an existing task ID from the database first
        Page<TaskDTO> existingTasks = taskService.getTasks(null, null, 0, 1);

        if (existingTasks.getTotalElements() > 0) {
            Integer validId = existingTasks.getContent().get(0).getTaskId();

            // Act: Call the API we are actually testing
            TaskDetailsDTO result = taskService.getTaskById(validId);

            // Assert
            Assertions.assertNotNull(result);
            Assertions.assertEquals(validId, result.getTaskId());
            Assertions.assertNotNull(result.getTaskName());
        }
    }

    @Test
    void testGetTaskById_notFound() { // NEGATIVE
        // ID 999999 is practically guaranteed not to exist in your MySQL DB
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                taskService.getTaskById(999999)
        );
    }
}