package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @Column(name = "CommentID")
    private Integer commentId;

    @Column(name = "Text")
    private String text;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "TaskID")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;
}