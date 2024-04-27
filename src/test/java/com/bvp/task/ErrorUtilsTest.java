package com.bvp.task;

import com.bvp.task.errorhandling.ErrorResponse;
import com.bvp.task.errorhandling.ErrorUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ErrorUtilsTest {

    @Test
    public void testBuildExceptionBody() {
        Exception exception = new Exception("Test Exception");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ResponseEntity<Object> responseEntity = ErrorUtils.buildExceptionBody(exception, httpStatus);
        assertEquals(httpStatus, responseEntity.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertEquals(httpStatus.value(), errorResponse.getStatusCode());
        assertEquals(httpStatus.name(), errorResponse.getMessage());
        assertEquals(List.of("Test Exception"), errorResponse.getDetails());
    }

    @Test
    public void testHandleValidationErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("objectName", "field1", "Error Message 1");
        FieldError fieldError2 = new FieldError("objectName", "field2", "Error Message 2");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        List<String> result = ErrorUtils.handleValidationErrors(bindingResult);
        assertEquals(List.of("Error Message 1", "Error Message 2"), result);
    }
}
