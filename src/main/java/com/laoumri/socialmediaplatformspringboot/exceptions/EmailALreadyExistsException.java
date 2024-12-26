package com.laoumri.socialmediaplatformspringboot.exceptions;

public class EmailALreadyExistsException extends RuntimeException{
    public EmailALreadyExistsException(String message){
        super(message);
    }
}
