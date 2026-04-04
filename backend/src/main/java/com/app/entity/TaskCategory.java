package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TaskCategory")
@Getter
@Setter
@NoArgsConstructor
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
}