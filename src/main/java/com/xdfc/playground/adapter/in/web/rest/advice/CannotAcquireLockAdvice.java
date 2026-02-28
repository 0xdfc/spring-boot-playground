package com.xdfc.playground.adapter.in.web.rest.advice;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class CannotAcquireLockAdvice {
    @ExceptionHandler(CannotAcquireLockException.class)
    public ResponseEntity<ProblemDetail> handleCannotAcquireLock(
        final CannotAcquireLockException exception
    ) {
        final ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT,
            "validation.resource.already.modified"
        );

        return ResponseEntity.of(problem).build();
    }
}
