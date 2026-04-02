package com.app.controller;

import com.app.dto.ProjectDTO;
import com.app.service.ProjectClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectWebController {

    private final ProjectClientService projectClientService;

    @GetMapping("/projects")
    public String viewProjects(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Model model) {

        try {
            List<ProjectDTO> projects = projectClientService.fetchProjects(startDate, endDate);
            model.addAttribute("projects", projects);
            model.addAttribute("error", null);
        } catch (Exception e) {
            model.addAttribute("projects", null);
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "projects";
    }
}