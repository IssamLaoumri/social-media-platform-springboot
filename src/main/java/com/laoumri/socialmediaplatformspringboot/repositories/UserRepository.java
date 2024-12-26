package com.laoumri.socialmediaplatformspringboot.repositories;

import com.laoumri.socialmediaplatformspringboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String email);
    boolean existsByUsername(String email);
}