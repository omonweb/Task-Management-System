package com.app.controller;

import com.app.dto.ProjectDTO;
import com.app.dto.TaskDTO;
import com.app.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // API 1
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<ProjectDTO> projects = projectService.getAllProjects(startDate, endDate);
        return ResponseEntity.ok(projects);
    }

    // API 2
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByProject(
            @PathVariable Integer id,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(projectService.getTasksByProject(id, status));
    }

}