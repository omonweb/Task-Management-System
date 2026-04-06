package com.app.controller;

import com.app.dto.PaginatedResponse;
import com.app.dto.TaskDTO;
import com.app.service.TaskClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TaskWebController {

    private final TaskClientService taskClientService;

    @GetMapping("/tasks")
    public String viewTasks(
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, // This is the size we need to remember
            Model model) {

        PaginatedResponse<TaskDTO> response = taskClientService.fetchTasks(priority, status, page, size);

        model.addAttribute("tasks", response.getContent());
        model.addAttribute("currentPage", response.getNumber());
        model.addAttribute("totalPages", response.getTotalPages());
        model.addAttribute("isFirst", response.isFirst());
        model.addAttribute("isLast", response.isLast());

        model.addAttribute("selectedPriority", priority);
        model.addAttribute("selectedStatus", status);

        // NEW: Pass the current size to the view
        model.addAttribute("currentSize", size);

        return "tasks";
    }
}