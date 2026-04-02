package com.app.service;

import com.app.dto.ProjectDTO;
import com.app.dto.TaskDTO;
import com.app.entity.Project;
import com.app.entity.Task;
import com.app.entity.User;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.ProjectRepository;
import com.app.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    // API 1

    // POSITIVE TEST - valid data returns correct project list
    @Test
    void getAllProjects_ShouldReturnProjects_WhenProjectsExist() {

        // dummy owner
        User fakeUser = new User();
        fakeUser.setUserId(1);
        fakeUser.setFullName("John Doe");

        // dummy project linked to owner
        Project fakeProject = new Project();
        fakeProject.setProjectId(1);
        fakeProject.setProjectName("Project One");
        fakeProject.setStartDate(LocalDate.of(2022, 1, 1));
        fakeProject.setEndDate(LocalDate.of(2022, 2, 1));
        fakeProject.setUser(fakeUser);

        when(projectRepository.findAll()).thenReturn(List.of(fakeProject));

        List<ProjectDTO> result = projectService.getAllProjects(null, null);

        // owner name and project fields should map correctly
        assertEquals(1, result.size());
        assertEquals("Project One", result.get(0).getProjectName());
        assertEquals("John Doe", result.get(0).getOwnerName());
        assertEquals(LocalDate.of(2022, 1, 1), result.get(0).getStartDate());
    }

    // NEGATIVE TEST - end date before start date should throw error
    @Test
    void getAllProjects_ShouldThrowException_WhenEndDateIsBeforeStartDate() {

        LocalDate startDate = LocalDate.of(2022, 6, 1);
        LocalDate endDate   = LocalDate.of(2022, 1, 1); // invalid range

        assertThrows(
                IllegalArgumentException.class,
                () -> projectService.getAllProjects(startDate, endDate)
        );

        // DB should not be hit for bad input
        verify(projectRepository, never()).findByStartDateBetween(any(), any());
    }

    // API 2

    // POSITIVE TEST - valid project id returns its tasks
    @Test
    void getTasksByProject_ShouldReturnTasks_WhenProjectExists() {

        // dummy project
        Project fakeProject = new Project();
        fakeProject.setProjectId(1);
        fakeProject.setProjectName("Project One");

        // dummy assigned user
        User fakeUser = new User();
        fakeUser.setUsername("john_doe");

        // dummy task inside the project
        Task fakeTask = new Task();
        fakeTask.setTaskId(1);
        fakeTask.setTaskName("Task One");
        fakeTask.setStatus("Pending");
        fakeTask.setPriority("High");
        fakeTask.setDueDate(LocalDate.of(2022, 1, 10));
        fakeTask.setProject(fakeProject);
        fakeTask.setUser(fakeUser);

        when(projectRepository.findById(1)).thenReturn(Optional.of(fakeProject));
        when(taskRepository.findByProjectProjectId(1)).thenReturn(List.of(fakeTask));

        List<TaskDTO> result = projectService.getTasksByProject(1, null);

        // task fields and joins should be mapped correctly
        assertEquals(1, result.size());
        assertEquals("Task One", result.get(0).getTaskName());
        assertEquals("Pending", result.get(0).getStatus());
        assertEquals("john_doe", result.get(0).getUserName());
        assertEquals("Project One", result.get(0).getProjectName());
    }

    // NEGATIVE TEST - wrong project id should throw 404
    @Test
    void getTasksByProject_ShouldThrowException_WhenProjectNotFound() {

        // project 999 does not exist
        when(projectRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.getTasksByProject(999, null)
        );

        assertEquals("Project not found with id: 999", exception.getMessage());

        // task repo should never be touched if project is missing
        verify(taskRepository, never()).findByProjectProjectId(any());
    }

}