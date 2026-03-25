package com.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "UserRoles")
@Getter
@Setter
@NoArgsConstructor
public class UserRoles {

    @EmbeddedId
    private UserRolesId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @MapsId("userRoleId")
    @JoinColumn(name = "UserRoleID")
    private UserRole userRole;
}