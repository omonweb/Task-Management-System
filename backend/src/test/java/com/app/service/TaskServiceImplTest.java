package com.app.service;

import com.app.dto.TaskDTO;
import com.app.dto.TaskDetailsDTO;
import com.app.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TaskServiceImplTest {

    @Autowired
    private TaskServiceImpl taskService;

    // API 1: getTasks (Paginated & Filtered)

    @Test
    void testGetTasks_success() { // POSITIVE
        Page<TaskDTO> result = taskService.getTasks(null, null, 0, 5);

        assertNotNull(result);

        // If the DB has data, verify the fields mapped correctly
        if (result.getTotalElements() > 0) {
            TaskDTO dto = result.getContent().get(0);
            assertNotNull(dto.getTaskId());
            assertNotNull(dto.getTaskName());
            assertNotNull(dto.getStatus());
        }
    }

    @Test
    void testGetTasks_invalidPagination() { // NEGATIVE
        // Spring Data JPA throws an IllegalArgumentException if page size is < 1
        assertThrows(IllegalArgumentException.class, () ->
                taskService.getTasks(null, null, 0, 0)
        );
    }

    // API 2: getTaskById (Deep Dive Details)

    @Test
    void testGetTaskById_success() { // POSITIVE
        // Dynamically grab an existing task ID from the database first
        Page<TaskDTO> existingTasks = taskService.getTasks(null, null, 0, 1);

        if (existingTasks.getTotalElements() > 0) {
            Integer validId = existingTasks.getContent().get(0).getTaskId();

            // Act: Call the API we are actually testing
            TaskDetailsDTO result = taskService.getTaskById(validId);

            // Assert
            assertNotNull(result);
            assertEquals(validId, result.getTaskId());
            assertNotNull(result.getTaskName());
        }
    }

    @Test
    void testGetTaskById_notFound() { // NEGATIVE
        // ID 999999 is practically guaranteed not to exist in your MySQL DB
        assertThrows(ResourceNotFoundException.class, () ->
                taskService.getTaskById(999999)
        );
    }




    /* Scenario: Call getTasks() with both priority = "High" AND status = "Pending".
    Assert that every task in the result has priority High AND status Pending.

    @Test
    void testGetTasksByPriorityandStatus() {
        Page<TaskDTO> results = taskService.getTasks("high","pending",0,10);

        assertNotNull(results);

        if(results.getTotalElements() > 0) {
            for(TaskDTO task : results.getContent()) {
                assertEquals("high",task.getPriority().toLowerCase());
                assertEquals("pending",task.getStatus().toLowerCase());
            }
        }
    }

    if (page > 0 && page >= taskPage.getTotalPages() && taskPage.getTotalElements() > 0) {
            throw new ResourceNotFoundException("Page index out of bounds. Total pages available: " + taskPage.getTotalPages());
            }


    @Test
    void testInvalidPagination() {
        //checking for the firstpage
        Page<TaskDTO> firstPage = taskService.getTasks(null,null,0,10);

        assertNotNull(firstPage);

        int pages = firstPage.getTotalPages();

        if(pages > 0) {
            assertThrows(ResourceNotFoundException.class, () ->
                    taskService.getTasks(null, null, pages, 10)
            );
        }
    }
    */
}