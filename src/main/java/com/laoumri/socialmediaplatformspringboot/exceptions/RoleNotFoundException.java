package com.laoumri.socialmediaplatformspringboot.exceptions;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 26/12/2024
 * Version: 0.0.1
 */
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
