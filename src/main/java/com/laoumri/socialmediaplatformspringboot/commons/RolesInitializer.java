package com.laoumri.socialmediaplatformspringboot.commons;

import com.laoumri.socialmediaplatformspringboot.entities.Role;
import com.laoumri.socialmediaplatformspringboot.enums.ERole;
import com.laoumri.socialmediaplatformspringboot.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 26/12/2024
 * Version: 0.0.1
 */

@Configuration
@Slf4j
public class RolesInitializer {
    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            Role c1 = new Role(null, ERole.ROLE_USER);
            Role c2 = new Role(null, ERole.ROLE_ADMIN);
            List<Role> roles = List.of(c1, c2);
            roleRepository.saveAll(roles);

            log.info("{} roles have been initialized in the database.", roles.size());
        };
    }

}
