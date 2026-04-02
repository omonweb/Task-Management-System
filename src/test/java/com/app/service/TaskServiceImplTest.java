package com.app.service;

import com.app.dto.TaskDTO;
import com.app.dto.TaskDetailsDTO;
import com.app.entity.Category;
import com.app.entity.Project;
import com.app.entity.Task;
import com.app.entity.TaskCategory;
import com.app.entity.User;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task dummyTask;
    private TaskDTO dummyTaskDTO;

    @BeforeEach
    void setUp() {
        Project project = new Project();
        project.setProjectName("Alpha");

        User user = new User();
        user.setUsername("developer1");

        dummyTask = new Task();
        dummyTask.setTaskId(1);
        dummyTask.setTaskName("Write Tests");
        dummyTask.setPriority("High");
        dummyTask.setStatus("Pending");
        dummyTask.setProject(project);
        dummyTask.setUser(user);

        dummyTaskDTO = new TaskDTO();
        dummyTaskDTO.setTaskId(1);
        dummyTaskDTO.setTaskName("Write Tests");
        dummyTaskDTO.setPriority("High");
        dummyTaskDTO.setStatus("Pending");
    }

    // --- TESTS FOR API 1: getTasks (Paginated & Filtered) ---

    @Test
    void getTasks_ShouldFilterByPriorityAndStatus() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(List.of(dummyTask), pageable, 1);

        when(taskRepository.findByPriorityIgnoreCaseAndStatusIgnoreCase(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(taskPage);
        when(modelMapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(dummyTaskDTO);

        // Act
        Page<TaskDTO> result = taskService.getTasks("High", "Pending", 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("High", result.getContent().get(0).getPriority());
        assertEquals("Pending", result.getContent().get(0).getStatus());

        verify(taskRepository, times(1))
                .findByPriorityIgnoreCaseAndStatusIgnoreCase("High", "Pending", pageable);
        verify(taskRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void getTasks_ShouldThrowException_WhenPageIsOutOfBounds() {
        // Arrange
        int requestedPage = 1; // User requests page 1 (the second page)
        int size = 10;
        Pageable pageable = PageRequest.of(requestedPage, size);

        Page<Task> taskPage = new PageImpl<>(List.of(dummyTask), PageRequest.of(0, size), 1);

        when(taskRepository.findAll(any(Pageable.class))).thenReturn(taskPage);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTasks(null, null, requestedPage, size);
        });

        assertTrue(exception.getMessage().contains("Page index out of bounds"));
        verify(modelMapper, never()).map(any(), any());
    }

    // --- TESTS FOR API 2: getTaskById (Deep Dive Details) ---

    // POSITIVE TEST
    @Test
    void getTaskById_ShouldReturnTaskDetails_WhenTaskExists() {
        // Arrange
        Integer taskId = 1;

        // 1. Create fake categories
        Category cat1 = new Category();
        cat1.setCategoryName("Development");
        TaskCategory tc1 = new TaskCategory();
        tc1.setCategory(cat1);

        Category cat2 = new Category();
        cat2.setCategoryName("Design");
        TaskCategory tc2 = new TaskCategory();
        tc2.setCategory(cat2);

        // 2. Attach categories to our dummy task
        dummyTask.setTaskCategories(List.of(tc1, tc2));

        // 3. Create a fake mapped DTO
        TaskDetailsDTO dummyDetailsDTO = new TaskDetailsDTO();
        dummyDetailsDTO.setTaskId(taskId);
        dummyDetailsDTO.setTaskName("Write Tests");

        // 4. Mock the repository and mapper behaviors
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(dummyTask));
        when(modelMapper.map(any(Task.class), eq(TaskDetailsDTO.class))).thenReturn(dummyDetailsDTO);

        // Act
        TaskDetailsDTO result = taskService.getTaskById(taskId);

        // Assert
        assertNotNull(result);

        // Verify manual mappings worked
        assertEquals("Alpha", result.getProjectName());
        assertEquals("developer1", result.getUserName());

        // Verify Category extraction logic worked
        assertNotNull(result.getCategories());
        assertEquals(2, result.getCategories().size());
        assertTrue(result.getCategories().contains("Development"));
        assertTrue(result.getCategories().contains("Design"));

        verify(taskRepository, times(1)).findById(taskId);
    }

    // NEGATIVE TEST
    @Test
    void getTaskById_ShouldThrowException_WhenTaskNotFound() {
        // Arrange
        Integer nonExistentTaskId = 999;

        // Mock the DB returning empty for this ID
        when(taskRepository.findById(nonExistentTaskId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(nonExistentTaskId);
        });

        assertEquals("Task not found with ID: 999", exception.getMessage());

        // Verify we hit the database but NEVER hit the model mapper because it crashed exactly when it was supposed to
        verify(taskRepository, times(1)).findById(nonExistentTaskId);
        verify(modelMapper, never()).map(any(), any());
    }
}