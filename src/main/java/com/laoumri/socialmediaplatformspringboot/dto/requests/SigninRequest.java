package com.laoumri.socialmediaplatformspringboot.dto.requests;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {
    @Email
    private String email;
    private String password;
}
