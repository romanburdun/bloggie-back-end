package com.bloggie.server.repositories;

import com.bloggie.server.domain.Role;
import com.bloggie.server.domain.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
    Boolean existsByName(RoleName name);
}
