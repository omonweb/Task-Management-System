package com.app.service;

import com.app.dto.ProjectDTO;
import com.app.entity.Project;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<ProjectDTO> getAllProjects(LocalDate startDate, LocalDate endDate) {

        List<Project> projects;

        if (startDate != null && endDate != null) {

            // Exception: endDate must be after startDate
            if (endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("endDate must be after startDate");
            }

            projects = projectRepository.findByStartDateBetween(startDate, endDate);

        } else {
            projects = projectRepository.findAll();
        }

        // Exception: no projects found
        if (projects.isEmpty()) {
            throw new ResourceNotFoundException("No projects found");
        }

        // Convert to DTO
        List<ProjectDTO> result = new ArrayList<>();

        for (Project p : projects) {
            ProjectDTO dto = new ProjectDTO();
            dto.setProjectId(p.getProjectId());
            dto.setProjectName(p.getProjectName());
            dto.setStartDate(p.getStartDate());
            dto.setEndDate(p.getEndDate());

            // Handle project with no owner
            if (p.getUser() != null) {
                dto.setOwnerName(p.getUser().getFullName());
            } else {
                dto.setOwnerName("No Owner Assigned");
            }

            result.add(dto);
        }

        return result;
    }
}