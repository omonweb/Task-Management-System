package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Attachment")
@Getter
@Setter
@NoArgsConstructor
public class Attachment {

    @Id
    @Column(name = "AttachmentID")
    private Integer attachmentId;

    @Column(name = "FileName", nullable = false)
    private String fileName;

    @Column(name = "FilePath", nullable = false)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "TaskID")
    private Task task;
}