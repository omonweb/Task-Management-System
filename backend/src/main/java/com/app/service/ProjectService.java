package com.app.service;

import com.app.dto.ProjectDTO;
import com.app.dto.TaskDTO;
import java.time.LocalDate;
import java.util.List;

public interface ProjectService {

    // API 1
    List<ProjectDTO> getAllProjects(LocalDate startDate, LocalDate endDate);

    // API 2
    List<TaskDTO> getTasksByProject(Integer projectId, String status);

}