package com.app.service;

import com.app.dto.CategoryDTO;
import com.app.entity.Category;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.CategoryRepository;
import com.app.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    @Override
    public CategoryDTO getCategoryWithTaskCounts(Integer categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + categoryId));

        long totalTasks      = taskRepository.countByCategoryId(categoryId);
        long completedTasks  = taskRepository.countByCategoryIdAndStatus(categoryId, "Completed");
        long pendingTasks    = taskRepository.countByCategoryIdAndStatus(categoryId, "Pending");
        long inProgressTasks = taskRepository.countByCategoryIdAndStatus(categoryId, "In Progress");

        return new CategoryDTO(
                category.getCategoryId(),
                category.getCategoryName(),
                totalTasks,
                completedTasks,
                pendingTasks,
                inProgressTasks
        );
    }
}