package com.app.service;

import com.app.dto.CategoryDTO;
import com.app.dto.TaskDTO;
import com.app.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoryServiceImplTest {


    @Autowired
    private CategoryServiceImpl categoryService;

    // API 1 positive - category id 2 exists in our DB so counts should return
    @Test
    public void testGetCategoryWithTaskCounts_WhenCategoryExists() {

        CategoryDTO result = categoryService.getCategoryWithTaskCounts(2);

        assertNotNull(result.getCategoryName());

        // total tasks should be 0 or more
        assertTrue(result.getTotalTasks() >= 0);
    }

    // API 1 negative - category 999 does not exist in DB so should throw 404
    @Test
    public void testGetCategoryWithTaskCounts_WhenCategoryNotFound() {

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getCategoryWithTaskCounts(999)
        );

        assertEquals("Category not found with id: 999", exception.getMessage());
    }

    // API 2 positive - "Design" category exists in our DB so tasks should return
    @Test
    public void testGetTasksByCategoryName_WhenCategoryExists() {

        List<TaskDTO> result = categoryService.getTasksByCategoryName("Design");

        assertFalse(result.isEmpty());

        // task name should not be null
        assertNotNull(result.get(0).getTaskName());
    }

    // API 2 negative - "xyz" does not exist in DB so should throw 404
    @Test
    public void testGetTasksByCategoryName_WhenCategoryNotFound() {

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getTasksByCategoryName("xyz")
        );

        assertEquals("No tasks found for category: xyz", exception.getMessage());
    }
}