package com.app.service;

import com.app.dto.CategoryDTO;
import com.app.entity.Category;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.CategoryRepository;
import com.app.repository.TaskCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TaskCategoryRepository taskCategoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               TaskCategoryRepository taskCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    @Override
    public CategoryDTO getCategoryWithTaskCounts(Integer categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + categoryId));

        long totalTasks      = taskCategoryRepository.countByCategory_CategoryId(categoryId);
        long completedTasks  = taskCategoryRepository.countByCategory_CategoryIdAndTask_StatusIgnoreCase(categoryId, "Completed");
        long pendingTasks    = taskCategoryRepository.countByCategory_CategoryIdAndTask_StatusIgnoreCase(categoryId, "Pending");
        long inProgressTasks = taskCategoryRepository.countByCategory_CategoryIdAndTask_StatusIgnoreCase(categoryId, "In Progress");

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