package com.laoumri.socialmediaplatformspringboot.dto.responses;

import com.laoumri.socialmediaplatformspringboot.enums.ErrorCode;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Integer statusCode;
    private HttpStatus status;
    private String reason;
    private String message;
    private ErrorCode errorCode;

    private Instant timestamp;
}
