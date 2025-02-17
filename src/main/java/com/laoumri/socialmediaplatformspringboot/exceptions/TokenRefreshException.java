package com.laoumri.socialmediaplatformspringboot.exceptions;

import com.laoumri.socialmediaplatformspringboot.enums.Code;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 14/01/2025
 * Version: 0.0.1
 */
@Getter
@Setter
public class TokenRefreshException extends RuntimeException{
    private final Code errorCode;
    public TokenRefreshException(String token, String message, Code errorCode) {
        super(String.format("Failed for [%s]: %s", token, message));
        this.errorCode = errorCode;
    }
}
