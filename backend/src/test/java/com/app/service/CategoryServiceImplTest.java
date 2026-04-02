package com.app.service;

import com.app.dto.CategoryDTO;
import com.app.dto.TaskDTO;
import com.app.entity.Category;
import com.app.entity.Task;
import com.app.entity.TaskCategory;
import com.app.entity.TaskCategoryId;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.CategoryRepository;
import com.app.repository.TaskCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    // API 1 tests
    // POSITIVE TEST CASE - When category EXISTS → should return correct CategoryDTO
    @Test
    public void testGetCategoryWithTaskCounts_WhenCategoryExists() {

        // STEP 1 - create a fake Category object
        Category fakeCategory = new Category();
        fakeCategory.setCategoryId(2);
        fakeCategory.setCategoryName("Design");

        // STEP 2 - tell fake repository what to return when id=1 is asked
        when(categoryRepository.findById(2)).thenReturn(Optional.of(fakeCategory));

        // STEP 3 - tell fake taskCategoryRepository what counts to return
        when(taskCategoryRepository.countByCategory_CategoryId(2)).thenReturn(4L);
        when(taskCategoryRepository.countByCategory_CategoryIdAndTask_StatusIgnoreCase(2, "Completed")).thenReturn(1L);
        when(taskCategoryRepository.countByCategory_CategoryIdAndTask_StatusIgnoreCase(2, "Pending")).thenReturn(1L);
        when(taskCategoryRepository.countByCategory_CategoryIdAndTask_StatusIgnoreCase(2, "In Progress")).thenReturn(2L);

        // STEP 4 - call the actual service method
        CategoryDTO result = categoryService.getCategoryWithTaskCounts(2);

        // STEP 5 - check the result is correct
        assertEquals(2, result.getCategoryId());
        assertEquals("Design", result.getCategoryName());
        assertEquals(4L, result.getTotalTasks());
        assertEquals(1L, result.getCompletedTasks());
        assertEquals(1L, result.getPendingTasks());
        assertEquals(2L, result.getInProgressTasks());
    }

    // NEGATIVE TEST CASE - When category DOES NOT EXIST → should throw 404 error
    @Test
    public void testGetCategoryWithTaskCounts_WhenCategoryNotFound() {

        // STEP 1 - tell fake repository to return empty (nothing found)
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        // STEP 2 - call the service and expect it to throw ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getCategoryWithTaskCounts(999)
        );

        // STEP 3 - check the error message is correct
        assertEquals("Category not found with id: 999", exception.getMessage());
    }



    // API 2 tests
    // POSITIVE TEST CASE - When category name EXISTS → should return list of tasks
    @Test
    public void testGetTasksByCategoryName_WhenCategoryExists() {

        // STEP 1 - create a fake Category object
        Category fakeCategory = new Category();
        fakeCategory.setCategoryId(2);
        fakeCategory.setCategoryName("Design");

        // STEP 2 - create a fake Task object
        Task fakeTask = new Task();
        fakeTask.setTaskId(1);
        fakeTask.setTaskName("Task One");
        fakeTask.setStatus("In Progress");
        fakeTask.setPriority("High");

        // STEP 3 - create a fake TaskCategory object
        // TaskCategory is the bridge between Task and Category
        TaskCategory fakeTaskCategory = new TaskCategory();
        fakeTaskCategory.setId(new TaskCategoryId());
        fakeTaskCategory.setTask(fakeTask);
        fakeTaskCategory.setCategory(fakeCategory);

        // STEP 4 - tell fake repository what to return when "Design" is asked
        when(taskCategoryRepository.findByCategory_CategoryNameIgnoreCase("Design"))
                .thenReturn(List.of(fakeTaskCategory));

        // STEP 5 - call the actual service method
        List<TaskDTO> result = categoryService.getTasksByCategoryName("Design");

        // STEP 6 - check the result is correct
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getTaskId());
        assertEquals("Task One", result.get(0).getTaskName());
        assertEquals("In Progress", result.get(0).getStatus());
        assertEquals("High", result.get(0).getPriority());
    }

    // NEGATIVE TEST CASE - When category name DOES NOT EXIST → should throw 404 error
    @Test
    public void testGetTasksByCategoryName_WhenCategoryNotFound() {

        // STEP 1 - tell fake repository to return empty list (nothing found)
        when(taskCategoryRepository.findByCategory_CategoryNameIgnoreCase("xyz"))
                .thenReturn(Collections.emptyList());

        // STEP 2 - call the service and expect it to throw ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getTasksByCategoryName("xyz")
        );

        // STEP 3 - check the error message is correct
        assertEquals("No tasks found for category: xyz", exception.getMessage());
    }
}