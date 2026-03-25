package com.app.repository;

import com.app.entity.TaskCategory;
import com.app.entity.TaskCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCategoryRepository extends JpaRepository<TaskCategory, TaskCategoryId> {
}