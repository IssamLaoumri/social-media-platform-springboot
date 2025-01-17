package com.laoumri.socialmediaplatformspringboot.exceptions;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 14/01/2025
 * Version: 0.0.1
 */
public class TokenRefreshException extends RuntimeException{
    public TokenRefreshException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
