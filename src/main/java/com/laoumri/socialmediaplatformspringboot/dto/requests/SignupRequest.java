package com.laoumri.socialmediaplatformspringboot.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignupRequest {
    @Size(min = 2, max = 50)
    private String firstname;

    @Size(min = 2, max = 50)
    private String lastname;

    @Email
    private String email;

    @Size(min = 6, max = 50)
    private String password;

    private int bDay;

    private int bMonth;

    private int bYear;

    private String gender;

    private Set<String> roles;
}
