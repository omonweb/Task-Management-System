package com.app.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class TaskCategoryId implements Serializable {

    private int taskId;
    private int categoryId;

    public TaskCategoryId() {}

    public TaskCategoryId(int taskId, int categoryId) {
        this.taskId = taskId;
        this.categoryId = categoryId;
    }

    // getters & setters
    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}