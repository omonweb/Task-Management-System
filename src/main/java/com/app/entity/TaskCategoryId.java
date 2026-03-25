package com.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class TaskCategoryId implements Serializable {

    @Column(name = "TaskID")
    private Integer taskId;

    @Column(name = "CategoryID")
    private Integer categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskCategoryId)) return false;
        TaskCategoryId that = (TaskCategoryId) o;
        return Objects.equals(taskId, that.taskId) &&
                Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, categoryId);
    }
}