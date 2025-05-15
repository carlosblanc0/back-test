package com.anonymousibex.Agents.of.Revature.util;

import com.anonymousibex.Agents.of.Revature.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public class ExceptionResponseUtils {

    public static ResponseEntity<ResponseDto> buildResponse(String message, HttpStatus status) {
        ResponseDto response = ResponseDto.builder()
                .message(message)
                .timestamp(Instant.now())
                .statusCode(status.value())
                .build();
        return new ResponseEntity<>(response, status);
    }
}
