package com.app.controller;

import com.app.dto.CategoryDTO;
import com.app.dto.TaskDTO;
import com.app.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET /api/categories/task-summary?id=1
    @GetMapping("/task-summary")
    public ResponseEntity<CategoryDTO> getCategoryTaskSummary(@RequestParam Integer id) {
        CategoryDTO summary = categoryService.getCategoryWithTaskCounts(id);
        return ResponseEntity.ok(summary);
    }

    // GET /api/categories/tasks?name=Development
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByCategoryName(
            @RequestParam String name) {
        return ResponseEntity.ok(categoryService.getTasksByCategoryName(name));
    }
}