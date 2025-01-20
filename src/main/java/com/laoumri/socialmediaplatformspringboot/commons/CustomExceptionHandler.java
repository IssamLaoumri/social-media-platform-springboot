package com.laoumri.socialmediaplatformspringboot.commons;

import com.laoumri.socialmediaplatformspringboot.dto.responses.ErrorResponse;
import com.laoumri.socialmediaplatformspringboot.enums.Code;
import com.laoumri.socialmediaplatformspringboot.enums.UserCode;
import com.laoumri.socialmediaplatformspringboot.exceptions.EmailALreadyExistsException;
import com.laoumri.socialmediaplatformspringboot.exceptions.RoleNotFoundException;
import com.laoumri.socialmediaplatformspringboot.exceptions.TokenRefreshException;
import com.laoumri.socialmediaplatformspringboot.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, List<String> message, Code errorCode) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status)
                .statusCode(status.value())
                .message(message)
                .reason(status.getReasonPhrase())
                .timestamp(Instant.now())
                .code(errorCode)
                .build();
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex){
        List<String> validationMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, validationMessages, UserCode.VALIDATION_ERROR);
    }


    @ExceptionHandler(EmailALreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(final EmailALreadyExistsException ex){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()), UserCode.EMAIL_ALREADY_EXISTS);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(final UserNotFoundException ex){
        return buildErrorResponse(HttpStatus.NOT_FOUND, List.of(ex.getMessage()), UserCode.USER_NOT_FOUND);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(final RoleNotFoundException ex){
        return buildErrorResponse(HttpStatus.NOT_FOUND, List.of(ex.getMessage()), UserCode.ROLE_NOT_FOUND);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorResponse> handleTokenRefreshException(final TokenRefreshException ex){
        return buildErrorResponse(HttpStatus.FORBIDDEN, List.of(ex.getMessage()), ex.getErrorCode());
    }
}
