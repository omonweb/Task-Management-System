package com.app.service;

import com.app.dto.ProjectDTO;
import com.app.dto.TaskDTO;
import com.app.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// loads full Spring app with DB
@SpringBootTest
class ProjectServiceImplTest {

    // real service hitting real repo/DB
    @Autowired
    private ProjectServiceImpl projectService;

    // positive: DB has projects
    @Test
    void getAllProjects_ShouldReturnProjects_WhenProjectsExist() {
        List<ProjectDTO> result = projectService.getAllProjects(null, null);
        assertFalse(result.isEmpty());
        assertNotNull(result.get(0).getProjectName());
    }

    // negative: invalid date range
    @Test
    void getAllProjects_ShouldThrowException_WhenEndDateIsBeforeStartDate() {
        LocalDate startDate = LocalDate.of(2022, 6, 1);
        LocalDate endDate   = LocalDate.of(2022, 1, 1);
        assertThrows(
                IllegalArgumentException.class,
                () -> projectService.getAllProjects(startDate, endDate)
        );
    }

    // positive: project id 1 exists
    @Test
    void getTasksByProject_ShouldReturnTasks_WhenProjectExists() {
        List<TaskDTO> result = projectService.getTasksByProject(1, null);
        assertFalse(result.isEmpty());
        assertNotNull(result.get(0).getTaskName());
    }

    // negative: project id 999 not found
    @Test
    void getTasksByProject_ShouldThrowException_WhenProjectNotFound() {
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> projectService.getTasksByProject(999, null)
        );
        assertEquals("Project not found with id: 999", exception.getMessage());
    }
}
