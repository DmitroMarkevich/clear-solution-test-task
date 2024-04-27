package com.bvp.task.errorhandling;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ErrorResponse {
    @Schema(description = "HTTP status code indicating the type of error")
    private Integer statusCode;

    @Schema(example = "2024-04-04T17:01:12.266037100Z", description = "Timestamp indicating when the error response was created")
    private Instant timestamp;

    @Schema(description = "Unique error code representing the type of error")
    private String message;

    @Schema(description = "A descriptive message of the error")
    private List<?> details;
}