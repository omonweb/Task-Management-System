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

@ExtendWith(MockitoExtension.class) // enables Mockito support for this test class
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository; // mock of repository (no real DB call)

    @InjectMocks
    private ProjectServiceImpl projectService; // actual service with mocked dependencies injected

    // positive test - verifies that projects are returned correctly when data exists
    @Test
    void getAllProjects_ShouldReturnProjects_WhenProjectsExist() {

        User fakeUser = new User(); // creating dummy user object
        fakeUser.setUserId(1);
        fakeUser.setFullName("John Doe");

        Project fakeProject = new Project(); // creating dummy project object
        fakeProject.setProjectId(1);
        fakeProject.setProjectName("Project One");
        fakeProject.setStartDate(LocalDate.of(2022, 1, 1));
        fakeProject.setEndDate(LocalDate.of(2022, 2, 1));
        fakeProject.setUser(fakeUser);

        // mock repository call to return predefined project list
        when(projectRepository.findAll()).thenReturn(List.of(fakeProject));

        // calling service method
        List<ProjectDTO> result = projectService.getAllProjects(null, null);

        // verifying expected output
        assertEquals(1, result.size());
        assertEquals("Project One", result.get(0).getProjectName());
        assertEquals("John Doe", result.get(0).getOwnerName());
        assertEquals(LocalDate.of(2022, 1, 1), result.get(0).getStartDate());
    }

    // negative test - verifies that exception is thrown for invalid date range
    @Test
    void getAllProjects_ShouldThrowException_WhenEndDateIsBeforeStartDate() {

        LocalDate startDate = LocalDate.of(2022, 6, 1);
        LocalDate endDate   = LocalDate.of(2022, 1, 1); // invalid case (end before start)

        // expecting IllegalArgumentException for wrong input
        assertThrows(
                IllegalArgumentException.class,
                () -> projectService.getAllProjects(startDate, endDate)
        );

        // ensuring repository method is not called in this scenario
        verify(projectRepository, never()).findByStartDateBetween(any(), any());
    }

}