package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Project")
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @Column(name = "ProjectID")
    private Integer projectId;

    @Column(name = "ProjectName", nullable = false)
    private String projectName;

    @Column(name = "Description")
    private String description;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
}