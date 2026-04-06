package com.app.repository;

import com.app.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    // 1. Filter by Priority only
    Page<Task> findByPriorityIgnoreCase(String priority, Pageable pageable);

    // 2. Filter by Status only (NEW)
    Page<Task> findByStatusIgnoreCase(String status, Pageable pageable);

    // 3. Filter by BOTH Priority and Status
    Page<Task> findByPriorityIgnoreCaseAndStatusIgnoreCase(String priority, String status, Pageable pageable);

    // 4. (Implicitly inherited) findAll(Pageable pageable) for when neither is provided

    //countTaskByPriority()

    // New methods for Project API 2
    List<Task> findByProjectProjectId(Integer projectId);
    List<Task> findByProjectProjectIdAndStatusIgnoreCase(Integer projectId, String status);

    List<Task> findByStatusIgnoreCase(String status);

    List<Task> findByUser_UserId(Integer userUserId);

    List<Task> findByTaskNameContainsIgnoreCase(String taskName);

    @Query("SELECT t.status , COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> countTasksGroupedByStatus();

    @Query("SELECT t.priority, count(t) from Task t where t.status NOT IN ('completed') group by t.priority ")
    List<Object[]> countTasksGroupedByPriority();

    @Query("SELECT t.project.projectName, count(t) from Task t group by t.project.projectName")
    List<Object[]> countTasksGroupedByProjects();
}