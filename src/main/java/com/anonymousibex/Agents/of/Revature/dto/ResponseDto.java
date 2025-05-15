package com.anonymousibex.Agents.of.Revature.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class ResponseDto {
    private String message;
    private Instant timestamp;
    private int statusCode;
}
