package com.laoumri.socialmediaplatformspringboot.dto.responses;

import com.laoumri.socialmediaplatformspringboot.enums.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Author: Issam Laoumri
 * Email: contact@issamlaoumri.com
 * Date Created: 24/12/2024
 * Version: 0.0.1
 */

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    private Code code;
    private Instant timestamp;
    private Object data;
}
