package com.app.service;

import com.app.dto.ProjectDTO;
import com.app.dto.TaskDTO;
import com.app.entity.Project;
import com.app.entity.Task;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.ProjectRepository;
import com.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    // API 1
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

    // API 2
    @Override
    public List<TaskDTO> getTasksByProject(Integer projectId, String status) {

        // check if project exists
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Project not found with id: " + projectId));

        // get tasks - with or without status filter
        List<Task> tasks;

        if (status != null) {
            tasks = taskRepository.findByProjectProjectIdAndStatusIgnoreCase(projectId, status);
        } else {
            tasks = taskRepository.findByProjectProjectId(projectId);
        }

        // if no tasks found
        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No tasks found for project id: " + projectId);
        }

        // convert to DTO
        List<TaskDTO> result = new ArrayList<>();
        for (Task t : tasks) {
            TaskDTO dto = new TaskDTO();
            dto.setTaskId(t.getTaskId());
            dto.setTaskName(t.getTaskName());
            dto.setStatus(t.getStatus());
            dto.setPriority(t.getPriority());
            dto.setDueDate(t.getDueDate());
            if (t.getProject() != null) {
                dto.setProjectName(t.getProject().getProjectName());
            }
            if (t.getUser() != null) {
                dto.setUserName(t.getUser().getUsername());
            }
            result.add(dto);
        }

        return result;
    }
}