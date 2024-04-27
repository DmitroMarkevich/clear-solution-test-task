package com.bvp.task.errorhandling;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorUtils {

    public static ResponseEntity<Object> buildExceptionBody(Exception exception, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(ErrorResponse.builder()
                .statusCode(httpStatus.value())
                .timestamp(Instant.now())
                .message(httpStatus.name())
                .details(List.of(exception.getMessage()))
                .build()
        );
    }

    public static List<String> handleValidationErrors(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
    }
}
