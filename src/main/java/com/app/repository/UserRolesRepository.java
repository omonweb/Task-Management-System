package com.app.repository;

import com.app.entity.UserRoles;
import com.app.entity.UserRolesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRoles, UserRolesId> {
}