package com.app.service;

import com.app.dto.CategoryDTO;
import com.app.dto.TaskDTO;
import com.app.entity.Category;
import com.app.entity.TaskCategory;
import com.app.exception.ResourceNotFoundException;
import com.app.repository.CategoryRepository;
import com.app.repository.TaskCategoryRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

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

    // API 2 - Get all tasks under a category name
    @Override
    public List<TaskDTO> getTasksByCategoryName(String categoryName) {

        // find all task-category rows matching the name
        List<TaskCategory> taskCategories = taskCategoryRepository
                .findByCategory_CategoryNameIgnoreCase(categoryName);

        if (taskCategories.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No tasks found for category: " + categoryName);
        }

        List<TaskDTO> result = new ArrayList<>();

        for (TaskCategory tc : taskCategories) {
            TaskDTO dto = new TaskDTO();
            dto.setTaskId(tc.getTask().getTaskId());
            dto.setTaskName(tc.getTask().getTaskName());
            dto.setStatus(tc.getTask().getStatus());
            dto.setPriority(tc.getTask().getPriority());
            dto.setDueDate(tc.getTask().getDueDate());

            if (tc.getTask().getProject() != null)
                dto.setProjectName(tc.getTask().getProject().getProjectName());

            if (tc.getTask().getUser() != null)
                dto.setUserName(tc.getTask().getUser().getUsername());

            result.add(dto);
        }

        return result;
    }
}