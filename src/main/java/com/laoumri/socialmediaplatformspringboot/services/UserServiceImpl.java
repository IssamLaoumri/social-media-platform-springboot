package com.laoumri.socialmediaplatformspringboot.services;

import com.laoumri.socialmediaplatformspringboot.dto.requests.SignupRequest;
import com.laoumri.socialmediaplatformspringboot.entities.Role;
import com.laoumri.socialmediaplatformspringboot.entities.User;
import com.laoumri.socialmediaplatformspringboot.enums.ERole;
import com.laoumri.socialmediaplatformspringboot.exceptions.EmailALreadyExistsException;
import com.laoumri.socialmediaplatformspringboot.exceptions.RoleNotFoundException;
import com.laoumri.socialmediaplatformspringboot.exceptions.UserNotFoundException;
import com.laoumri.socialmediaplatformspringboot.repositories.RoleRepository;
import com.laoumri.socialmediaplatformspringboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createNewUser(SignupRequest request) {
        if(userRepository.existsByEmail(request.getEmail()))
            throw new EmailALreadyExistsException("Email : "+request.getEmail()+" already exists");

        Set<Role> roles = new HashSet<>();
        if(request.getRoles() == null) {
            Role role = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(()-> new RoleNotFoundException(ERole.ROLE_USER.name()));
            roles.add(role);
        } else  {
            request.getRoles().forEach(role -> {
                try {
                    Role roleEntity = roleRepository.findByName(ERole.valueOf(role))
                            .orElseThrow(()-> new RoleNotFoundException(role));
                    roles.add(roleEntity);
                }catch (IllegalArgumentException e){
                    throw new RoleNotFoundException(role);
                }
            });
        }

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .bDay(request.getBDay())
                .bMonth(request.getBMonth())
                .bYear(request.getBYear())
                .gender(request.getGender())
                .roles(roles)
                .username("issam")
                .build();

        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->
                new UserNotFoundException("User not found with email : "+email)
        );
    }
}
