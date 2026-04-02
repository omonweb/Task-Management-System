package com.app.service;

import com.app.dto.ProjectDTO;
import java.time.LocalDate;
import java.util.List;

public interface ProjectService {
    List<ProjectDTO> getAllProjects(LocalDate startDate, LocalDate endDate);
}