package com.xdfc.playground.adapter.in.web.rest.advice;

import com.xdfc.playground.adapter.in.web.rest.dto.message.ErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class IntegrityViolationAdvice {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleIntegrityViolation(
        final DataIntegrityViolationException exception
    ) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                new ErrorMessage("validation.unique.violation")
            );
    }
}
