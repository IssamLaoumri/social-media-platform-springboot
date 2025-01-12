package com.laoumri.socialmediaplatformspringboot.dto.responses;

import com.laoumri.socialmediaplatformspringboot.enums.EGender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 26/12/2024
 * Version: 0.0.1
 */

@Getter
@Setter
@Builder
public class AuthResponse {
    private String firstname;
    private String lastname;
    private String email;
    private int bDay;
    private int bMonth;
    private int bYear;
    private EGender gender;
    private List<String> roles;
}