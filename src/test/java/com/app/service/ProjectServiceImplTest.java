package com.app.service;

import com.app.dto.ProjectDTO;
import com.app.entity.Project;
import com.app.entity.User;
import com.app.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository; // fake repository

    @InjectMocks
    private ProjectServiceImpl projectService; // real service

    // positive test - returns projects successfully
    @Test
    void getAllProjects_ShouldReturnProjects_WhenProjectsExist() {

        User fakeUser = new User();
        fakeUser.setUserId(1);
        fakeUser.setFullName("John Doe");

        Project fakeProject = new Project();
        fakeProject.setProjectId(1);
        fakeProject.setProjectName("Project One");
        fakeProject.setStartDate(LocalDate.of(2022, 1, 1));
        fakeProject.setEndDate(LocalDate.of(2022, 2, 1));
        fakeProject.setUser(fakeUser);

        when(projectRepository.findAll()).thenReturn(List.of(fakeProject)); // mock DB call

        List<ProjectDTO> result = projectService.getAllProjects(null, null);

        // verify result
        assertEquals(1, result.size());
        assertEquals("Project One", result.get(0).getProjectName());
        assertEquals("John Doe", result.get(0).getOwnerName());
        assertEquals(LocalDate.of(2022, 1, 1), result.get(0).getStartDate());
    }

    // negative test - invalid date range throws exception
    @Test
    void getAllProjects_ShouldThrowException_WhenEndDateIsBeforeStartDate() {

        LocalDate startDate = LocalDate.of(2022, 6, 1);
        LocalDate endDate   = LocalDate.of(2022, 1, 1); // invalid - before startDate

        assertThrows(
                IllegalArgumentException.class,
                () -> projectService.getAllProjects(startDate, endDate)
        );

        verify(projectRepository, never()).findByStartDateBetween(any(), any()); // repo should not be called
    }

}