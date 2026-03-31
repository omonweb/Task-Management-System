package com.app.service;

import com.app.dto.TaskDTO;
import com.app.entity.Project;
import com.app.entity.Task;
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

    // POSITIVE TEST
    @Test
    void getTasks_ShouldFilterByPriorityAndStatus() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(List.of(dummyTask), pageable, 1);

        // Mock the specific dual-filter repository method
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

        // Verify that the correct repository method was called exactly once
        verify(taskRepository, times(1))
                .findByPriorityIgnoreCaseAndStatusIgnoreCase("High", "Pending", pageable);

        // Ensure the default findAll was NEVER called (proving our routing logic works)
        verify(taskRepository, never()).findAll(any(Pageable.class));
    }

    // NEGATIVE TEST
    @Test
    void getTasks_ShouldThrowException_WhenPageIsOutOfBounds() {
        // Arrange
        int requestedPage = 1; // User requests page 1 (the second page)
        int size = 10;
        Pageable pageable = PageRequest.of(requestedPage, size);

        // We simulate the database having only 1 item total, meaning there is only 1 page (page 0).
        Page<Task> taskPage = new PageImpl<>(List.of(dummyTask), PageRequest.of(0, size), 1);

        // We mock the default findAll method since null filters are passed in the Act step
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(taskPage);

        // Act & Assert
        // We assert that calling the method throws our expected ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTasks(null, null, requestedPage, size);
        });

        // Verify the exception message matches our implementation
        assertTrue(exception.getMessage().contains("Page index out of bounds"));

        // Verify modelMapper was never called because the exception stopped the execution early
        verify(modelMapper, never()).map(any(), any());
    }
}