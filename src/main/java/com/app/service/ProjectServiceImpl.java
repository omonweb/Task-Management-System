package com.app.service;

import com.app.dto.ProjectDTO;
import com.app.entity.Project;
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

        // Step 1 - get projects from database
        List<Project> projects;

        if (startDate != null && endDate != null) {
            // filter by date if dates are given
            projects = projectRepository.findByStartDateBetween(startDate, endDate);
        } else {
            // otherwise get all projects
            projects = projectRepository.findAll();
        }

        // Step 2 - convert to DTO
        List<ProjectDTO> result = new ArrayList<>();

        for (Project p : projects) {
            ProjectDTO dto = new ProjectDTO();
            dto.setProjectId(p.getProjectId());
            dto.setProjectName(p.getProjectName());
            dto.setStartDate(p.getStartDate());
            dto.setEndDate(p.getEndDate());

            // get owner name safely
            if (p.getUser() != null) {
                dto.setOwnerName(p.getUser().getFullName());
            } else {
                dto.setOwnerName("No Owner");
            }

            result.add(dto);
        }

        return result;
    }
}