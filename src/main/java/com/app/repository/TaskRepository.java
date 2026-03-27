package com.app.repository;

import com.app.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    // Count tasks linked to a category via the TaskCategory join table
    @Query("""
            SELECT COUNT(tc) FROM TaskCategory tc
            WHERE tc.category.categoryId = :categoryId
            """)
    long countByCategoryId(@Param("categoryId") Integer categoryId);

    @Query("""
            SELECT COUNT(tc) FROM TaskCategory tc
            WHERE tc.category.categoryId = :categoryId
            AND LOWER(tc.task.status) = LOWER(:status)
            """)
    long countByCategoryIdAndStatus(@Param("categoryId") Integer categoryId,
                                    @Param("status") String status);
}