package com.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TaskCategory")
public class TaskCategory {

    @EmbeddedId
    private TaskCategoryId id;

    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "TaskID")
    private Task task;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "CategoryID")
    private Category category;

    // Constructors
    public TaskCategory() {}

    public TaskCategory(Task task, Category category) {
        this.task = task;
        this.category = category;
        this.id = new TaskCategoryId(task.getTaskId(), category.getCategoryId());
    }

    // getters & setters
    public TaskCategoryId getId() { return id; }
    public void setId(TaskCategoryId id) { this.id = id; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}