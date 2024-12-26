package com.laoumri.socialmediaplatformspringboot.commons;

import com.laoumri.socialmediaplatformspringboot.dto.responses.ErrorResponse;
import com.laoumri.socialmediaplatformspringboot.enums.ErrorCode;
import com.laoumri.socialmediaplatformspringboot.exceptions.EmailALreadyExistsException;
import com.laoumri.socialmediaplatformspringboot.exceptions.RoleNotFoundException;
import com.laoumri.socialmediaplatformspringboot.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class CustomExceptionHandler {
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, ErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .statusCode(status.value())
                .message(message)
                .reason(status.getReasonPhrase())
                .timestamp(Instant.now())
                .errorCode(errorCode)
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }


    @ExceptionHandler(EmailALreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(final EmailALreadyExistsException ex){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(final UserNotFoundException ex){
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ErrorCode.USER_NOT_FOUND);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(final RoleNotFoundException ex){
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ErrorCode.ROLE_NOT_FOUND);
    }
}
