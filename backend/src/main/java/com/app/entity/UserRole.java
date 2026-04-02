package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "UserRole")
@Getter
@Setter
@NoArgsConstructor
public class UserRole {

    @Id
    @Column(name = "UserRoleID")
    private Integer userRoleId;

    @Column(name = "RoleName", nullable = false)
    private String roleName;
}