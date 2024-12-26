package com.laoumri.socialmediaplatformspringboot.repositories;

import com.laoumri.socialmediaplatformspringboot.entities.Role;
import com.laoumri.socialmediaplatformspringboot.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
