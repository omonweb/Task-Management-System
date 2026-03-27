package com.app.controller;

import com.app.dto.CategoryDTO;
import com.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // GET /api/categories/{id}/task-summary
    @GetMapping("/{id}/task-summary")
    public ResponseEntity<CategoryDTO> getCategoryTaskSummary(@PathVariable Integer id) {
        CategoryDTO summary = categoryService.getCategoryWithTaskCounts(id);
        return ResponseEntity.ok(summary);
    }
}