package com.laoumri.socialmediaplatformspringboot.services;

import com.laoumri.socialmediaplatformspringboot.dto.requests.SignupRequest;
import com.laoumri.socialmediaplatformspringboot.entities.User;

public interface UserService {
    User createNewUser(SignupRequest request);
    User getUserByEmail(String email);
}
