package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Task")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @Column(name = "TaskID")
    private Integer taskId;

    @Column(name = "TaskName", nullable = false)
    private String taskName;

    @Column(name = "Description")
    private String description;

    @Column(name = "DueDate")
    private LocalDate dueDate;

    @Column(name = "Priority")
    private String priority;

    @Column(name = "Status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "ProjectID")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
}