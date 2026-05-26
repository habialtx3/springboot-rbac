package com.habialtx3.rbac_sringboot.repository;

import com.habialtx3.rbac_sringboot.entity.Role;
import com.habialtx3.rbac_sringboot.entity.User;
import com.habialtx3.rbac_sringboot.model.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
