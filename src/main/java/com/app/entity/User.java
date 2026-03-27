package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "User")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "Username", nullable = false)
    private String username;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "FullName", nullable = false)
    private String fullName;

    @OneToMany(mappedBy = "user")
    private List<UserRoles> userRoles;
}