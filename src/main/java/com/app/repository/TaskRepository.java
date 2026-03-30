package com.app.repository;

import com.app.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    // 1. Filter by Priority only
    Page<Task> findByPriorityIgnoreCase(String priority, Pageable pageable);

    // 2. Filter by Status only (NEW)
    Page<Task> findByStatusIgnoreCase(String status, Pageable pageable);

    // 3. Filter by BOTH Priority and Status
    Page<Task> findByPriorityIgnoreCaseAndStatusIgnoreCase(String priority, String status, Pageable pageable);

    // 4. (Implicitly inherited) findAll(Pageable pageable) for when neither is provided
}