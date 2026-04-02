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
public class UserRolesId implements Serializable {

    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "UserRoleID")
    private Integer userRoleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRolesId)) return false;
        UserRolesId that = (UserRolesId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(userRoleId, that.userRoleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userRoleId);
    }
}