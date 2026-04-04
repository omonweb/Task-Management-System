package com.app.controller;

import com.app.dto.TaskDTO;
import com.app.service.CategoryClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryWebController {

    private final CategoryClientService categoryClientService;

    @GetMapping("/categories")
    public String viewCategories(
            @RequestParam(required = false) String name,
            Model model) {

        if (name != null && !name.isBlank()) {
            try {
                List<TaskDTO> tasks = categoryClientService.fetchTasksByCategoryName(name);
                model.addAttribute("tasks", tasks);
                model.addAttribute("taskCount", tasks.size());
                model.addAttribute("error", null);
            } catch (Exception e) {
                model.addAttribute("tasks", null);
                model.addAttribute("taskCount", 0);
                model.addAttribute("error", e.getMessage());
            }
        } else {
            model.addAttribute("tasks", null);
            model.addAttribute("taskCount", 0);
            model.addAttribute("error", null);
        }

        model.addAttribute("categoryName", name);

        return "categories";
    }
}