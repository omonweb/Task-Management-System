package com.app.controller;

import com.app.dto.CategoryDTO;
import com.app.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // GET /api/categories/{id}/task-summary
    @GetMapping("/task-summary")
    public ResponseEntity<CategoryDTO> getCategoryTaskSummary(@RequestParam Integer id) {
        CategoryDTO summary = categoryService.getCategoryWithTaskCounts(id);
        return ResponseEntity.ok(summary);
    }
}