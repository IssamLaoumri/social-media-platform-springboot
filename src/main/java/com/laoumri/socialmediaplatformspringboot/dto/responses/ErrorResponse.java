package com.laoumri.socialmediaplatformspringboot.dto.responses;

import com.laoumri.socialmediaplatformspringboot.enums.Code;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Integer statusCode;
    private HttpStatus status;
    private String reason;
    private List<String> message;
    private Code code;

    private Instant timestamp;
}
