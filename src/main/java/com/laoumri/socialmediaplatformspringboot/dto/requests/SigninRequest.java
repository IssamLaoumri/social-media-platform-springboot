package com.laoumri.socialmediaplatformspringboot.dto.requests;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {
    @Email
    private String email;
    private String password;
}
