package com.app.repository;

import com.app.entity.TaskCategory;
import com.app.entity.TaskCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, TaskCategoryId> {

    // Count all tasks under a category
    long countByCategory_CategoryId(Integer categoryId);

    // Count tasks under a category filtered by status (case-insensitive)
    long countByCategory_CategoryIdAndTask_StatusIgnoreCase(Integer categoryId, String status);
}