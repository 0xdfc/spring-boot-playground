package com.xdfc.playground.adapter.in.web.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

final public class NotFoundHttpException extends ResponseStatusException {
    public NotFoundHttpException() {
        super(HttpStatus.NOT_FOUND);
    }
}
