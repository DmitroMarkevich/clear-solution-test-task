package com.bvp.task.errorhandling;

import com.bvp.task.errorhandling.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    private ResponseEntity<Object> handleException(ExpiredJwtException exception) {
        return ErrorUtils.buildExceptionBody(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    private ResponseEntity<Object> handleException(SignatureException exception) {
        return ErrorUtils.buildExceptionBody(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<Object> handleException(BadCredentialsException exception) {
        return ErrorUtils.buildExceptionBody(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    private ResponseEntity<Object> handleException(NoSuchElementException exception) {
        return ErrorUtils.buildExceptionBody(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginException.class)
    private ResponseEntity<Object> handleException(LoginException exception) {
        return ErrorUtils.buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<Object> handleException(IllegalArgumentException exception) {
        return ErrorUtils.buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<Object> handleException(ItemNotCreatedException exception) {
        return ErrorUtils.buildExceptionBody(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<Object> handleException(AccessDeniedException exception) {
        return ErrorUtils.buildExceptionBody(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    private ResponseEntity<Object> handleException(UserAlreadyExistsException exception) {
        return ErrorUtils.buildExceptionBody(exception, HttpStatus.CONFLICT);
    }
}
